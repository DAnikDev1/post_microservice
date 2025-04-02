package src.danik.postservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import src.danik.postservice.dto.comment.CommentCreateDto;
import src.danik.postservice.dto.comment.CommentReadDto;
import src.danik.postservice.dto.comment.CommentUpdateDto;
import src.danik.postservice.entity.Comment;
import src.danik.postservice.entity.Post;
import src.danik.postservice.exception.DataValidationException;
import src.danik.postservice.kafka.event.NotificationEvent;
import src.danik.postservice.mapper.comment.CommentMapper;
import src.danik.postservice.repository.CommentRepository;
import src.danik.postservice.service.CommentService;
import src.danik.postservice.service.producer.NotificationProducer;
import src.danik.postservice.types.NotificationType;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserServiceImpl userService;
    private final PostServiceImpl postService;

    private final NotificationProducer notificationProducer;

    public Comment getCommentById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Comment with id = %d was not found", id)));
    }

    @Override
    public List<CommentReadDto> getCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId).stream().map(commentMapper::toReadDto).toList();
    }

    @Override
    @Transactional
    public CommentReadDto createComment(CommentCreateDto commentCreateDto) {
        if (!userService.isUserExist(commentCreateDto.getUserId())) {
            throw new DataValidationException("User doesn't exist");
        }
        Post post = postService.getPostById(commentCreateDto.getPostId());

        Comment comment = commentMapper.toEntity(commentCreateDto);
        comment.setPost(post);
        post.getComments().add(comment); // Возможно стоит инкапсулировать но не уверен

        Comment result = commentRepository.save(comment);
        log.info("Created new comment with id = {}", result.getId());
        CommentReadDto commentReadDto = commentMapper.toReadDto(result);
        sendNotificationAsNewComment(commentReadDto, post.getUserId());
        return commentReadDto;
    }

    @Override
    public void deleteComment(Long commentId) {

        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentReadDto updateComment(CommentUpdateDto commentUpdateDto) {
        Comment comment = getCommentById(commentUpdateDto.getCommentId());
        commentMapper.toUpdate(comment, commentUpdateDto);

        return commentMapper.toReadDto(commentRepository.save(comment));
    }

    public void sendNotificationAsNewComment(CommentReadDto commentReadDto, Long userId) {
        String username;
        try {
            username = userService.getUserById(commentReadDto.getUserId()).username();
        } catch (EntityNotFoundException e) {
            log.error("Impossible to send notification because user with id {} doesn't exist", commentReadDto.getUserId());
            return;
        }
        NotificationEvent event = NotificationEvent.builder()
                .eventId(commentReadDto.getCommentId())
                .userId(userId)
                .text(String.format("User with nickname %s wrote a comment under your post", username))
                .notificationType(NotificationType.NEW_COMMENT).build();
        log.info("Sending notification event to kafka: {}", event);
        try {
            notificationProducer.sendNotificationEvent(event);
        } catch (Exception e) {
            log.error("Error when trying send notification event: {}", event, e);
        }
    }
}
