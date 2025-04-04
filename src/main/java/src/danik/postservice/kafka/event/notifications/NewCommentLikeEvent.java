package src.danik.postservice.kafka.event.notifications;

public record NewCommentLikeEvent(
        Long likeId,
        Long likerId,
        Long commentAuthorId
) {
}
