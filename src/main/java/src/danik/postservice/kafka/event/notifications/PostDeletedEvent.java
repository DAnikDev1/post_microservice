package src.danik.postservice.kafka.event.notifications;

public record PostDeletedEvent(
        Long postAuthorId,
        Long postId
) {

}
