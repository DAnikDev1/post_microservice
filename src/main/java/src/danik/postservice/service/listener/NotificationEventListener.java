package src.danik.postservice.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import src.danik.dto.NotificationEvent;
import src.danik.dto.NotificationType;
import src.danik.postservice.kafka.event.notifications.*;
import src.danik.postservice.service.CommentService;
import src.danik.postservice.service.LikeService;
import src.danik.postservice.service.impl.UserServiceImpl;
import src.danik.postservice.service.producer.NotificationProducer;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationEventListener {

    private final NotificationProducer notificationProducer;
    private final UserServiceImpl userService;
    private final LikeService likeService;
    private final CommentService commentService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNewCommentEvent(NewCommentEvent commentEvent) {
        String username = userService.getUserById(commentEvent.commenterId()).username();
        Long postId = commentService.getCommentById(commentEvent.commentId()).getPost().getId();
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .eventId(commentEvent.commentId())
                .userId(commentEvent.postAuthorId())
                .text(String.format("User %s wrote a comment under your post with id = %d", username, postId))
                .notificationType(NotificationType.NEW_COMMENT)
                .build();
        sendPreparedNotificationEvent(notificationEvent);
    }
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPostPublishedEvent(PostPublishedEvent postPublishedEvent) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .eventId(postPublishedEvent.postId())
                .userId(postPublishedEvent.postAuthorId())
                .text(String.format("Your post was successfully published [id %s]", postPublishedEvent.postId()))
                .notificationType(NotificationType.POST_PUBLISHED)
                .build();
        sendPreparedNotificationEvent(notificationEvent);
    }
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPostDeletedEvent(PostDeletedEvent postDeletedEvent) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .eventId(postDeletedEvent.postId())
                .userId(postDeletedEvent.postAuthorId())
                .text(String.format("Your post was deleted [id %s]", postDeletedEvent.postId()))
                .notificationType(NotificationType.POST_DELETED)
                .build();
        sendPreparedNotificationEvent(notificationEvent);
    }
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNewPostLikeEvent(NewPostLikeEvent newPostLikeEvent) {
        String username = userService.getUserById(newPostLikeEvent.likerId()).username();
        Long postId = likeService.findById(newPostLikeEvent.likeId()).getPost().getId();
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .eventId(newPostLikeEvent.likeId())
                .userId(newPostLikeEvent.postAuthorId())
                .text(String.format("User %s put like on your post with id = %d", username, postId))
                .notificationType(NotificationType.NEW_POST_LIKE)
                .build();
        sendPreparedNotificationEvent(notificationEvent);
    }
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNewCommentLikeEvent(NewCommentLikeEvent newCommentLikeEvent) {
        String username = userService.getUserById(newCommentLikeEvent.likerId()).username();
        Long commentId = likeService.findById(newCommentLikeEvent.likeId()).getComment().getId();
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .eventId(newCommentLikeEvent.likeId())
                .userId(newCommentLikeEvent.commentAuthorId())
                .text(String.format("User %s put like on your comment with id = %d", username, commentId))
                .notificationType(NotificationType.NEW_COMMENT_LIKE)
                .build();
        sendPreparedNotificationEvent(notificationEvent);
    }


    public void sendPreparedNotificationEvent(NotificationEvent notificationEvent) {
        try {
            log.info("Sending notification event to kafka: {}", notificationEvent);
            notificationProducer.sendNotificationEvent(notificationEvent);
        } catch (Exception e) {
            log.error("Error when trying send notification event: {}", notificationEvent, e);
        }
    }
}
