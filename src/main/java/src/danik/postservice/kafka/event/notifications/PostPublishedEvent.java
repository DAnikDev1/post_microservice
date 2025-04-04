package src.danik.postservice.kafka.event.notifications;

public record PostPublishedEvent(
        Long postAuthorId,
        Long postId
) {
}
