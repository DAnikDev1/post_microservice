package src.danik.postservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import src.danik.postservice.dto.comment.CommentCreateDto;
import src.danik.postservice.dto.comment.CommentReadDto;
import src.danik.postservice.dto.comment.CommentUpdateDto;
import src.danik.postservice.entity.Comment;
import src.danik.postservice.entity.Post;
import src.danik.postservice.exception.DataValidationException;
import src.danik.postservice.mapper.comment.CommentMapper;
import src.danik.postservice.repository.CommentRepository;
import src.danik.postservice.service.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserServiceImpl userService;
    private final PostServiceImpl postService;

    public Comment getCommentById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Comment with id = %d was not found", id)));
    }

    @Override
    public List<CommentReadDto> getCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId).stream().map(commentMapper::toReadDto).toList();
    }

    @Override
    public CommentReadDto createComment(CommentCreateDto commentCreateDto) {
        if (!userService.isUserExist(commentCreateDto.getUserId())) {
            throw new DataValidationException("User doesn't exists");
        }
        Comment comment = commentMapper.toEntity(commentCreateDto);
        Post post = postService.getPostById(commentCreateDto.getPostId());
        comment.setPost(post);
        Comment result = commentRepository.save(comment);
        return commentMapper.toReadDto(result);
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
}
