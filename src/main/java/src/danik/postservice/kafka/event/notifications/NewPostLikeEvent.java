package src.danik.postservice.kafka.event.notifications;

public record NewPostLikeEvent(
        Long likeId,
        Long likerId,
        Long postAuthorId
) {
}
