package src.danik.postservice.kafka.event.notifications;

public record NewCommentEvent(
        Long commentId,
        Long commenterId,
        Long postAuthorId
) {
}
