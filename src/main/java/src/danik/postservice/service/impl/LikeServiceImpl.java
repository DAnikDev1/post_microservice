package src.danik.postservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import src.danik.postservice.dto.LikeDto;
import src.danik.postservice.dto.UserDto;
import src.danik.postservice.entity.Comment;
import src.danik.postservice.entity.Like;
import src.danik.postservice.entity.Post;
import src.danik.postservice.exception.DataValidationException;
import src.danik.postservice.kafka.event.notifications.NewCommentLikeEvent;
import src.danik.postservice.kafka.event.notifications.NewPostLikeEvent;
import src.danik.postservice.mapper.LikeMapper;
import src.danik.postservice.repository.LikeRepository;
import src.danik.postservice.repository.types.LikeType;
import src.danik.postservice.service.LikeService;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final UserServiceImpl userService;

    private final CommentServiceImpl commentService;
    private final PostServiceImpl postService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Like findById(@NotNull @Positive Long id) {
        return likeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Like with id = %d was not found", id)));
    }

    @Override
    @Transactional
    public LikeDto userLike(LikeDto likeDto, LikeType likeType) {
        UserDto userDto = userService.getUserByContext();
        Like like = assignLikeToPostOrComment(likeDto, likeType, userDto);
        Like result = likeRepository.save(like);

        publishLikeNotificationToListener(likeType, result);

        return likeToLikeDto(result, likeType);
    }
    private Like assignLikeToPostOrComment(LikeDto likeDto, LikeType likeType, UserDto userDto) {
        Like like = likeMapper.toEntity(likeDto);
        like.setUserId(userDto.id());
        if (likeType == LikeType.COMMENT) {
            if (likeRepository.findByCommentIdAndUserId(likeDto.likeId(), userDto.id()).isPresent()) {
                throw new DataValidationException("Comment already have like");
            }
            like.setComment(commentService.getCommentById(likeDto.likeId()));
        } else if (likeType == LikeType.POST) {
            if (likeRepository.findByPostIdAndUserId(likeDto.likeId(), userDto.id()).isPresent()) {
                throw new DataValidationException("Post already have like");
            }
            like.setPost(postService.getPostById(likeDto.likeId()));
        }
        return like;
    }
    private LikeDto likeToLikeDto(Like like, LikeType likeType) {
        if (likeType == LikeType.COMMENT) {
            return likeMapper.toLikeCommentDto(like);
        } else {
            return likeMapper.toLikePostDto(like);
        }
    }
    @Override
    public void removeLike(Long likeId, LikeType likeType, LikeDto likeDto) {
        UserDto userDto = userService.getUserByContext();
        if (likeType == LikeType.COMMENT) {
            if (cantUserRemoveLike(likeId, userDto.id())) {
                throw new DataValidationException("User cannot remove Like on comment because ID doesn't match");
            }
            likeRepository.deleteByCommentIdAndUserId(likeDto.likeId(), userDto.id());
        } else if (likeType == LikeType.POST) {
            if (cantUserRemoveLike(likeId, userDto.id())) {
                throw new DataValidationException("User cannot remove Like on post because ID doesn't match");
            }
            likeRepository.deleteByPostIdAndUserId(likeDto.likeId(), userDto.id());
        }
    }
    public void publishLikeNotificationToListener(LikeType likeType, Like like) {
        Long userId;
        switch (likeType) {
            case POST -> {
                Post likedPost = like.getPost();
                if (likedPost == null) return;
                userId = likedPost.getUserId();
                applicationEventPublisher.publishEvent(new NewPostLikeEvent(
                        like.getId(),
                        like.getUserId(),
                        userId));
            }
            case COMMENT -> {
                Comment comment = like.getComment();
                if (comment == null) return;
                userId = comment.getUserId();
                applicationEventPublisher.publishEvent(new NewCommentLikeEvent(
                        like.getId(), like.getUserId(), userId));
            }
        }
    }

    private boolean cantUserRemoveLike(Long likeId, Long userId) {
        return !findById(likeId).getUserId().equals(userId);
    }
}
