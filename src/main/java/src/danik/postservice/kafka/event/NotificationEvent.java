package src.danik.postservice.kafka.event;

import lombok.Builder;
import lombok.Data;
import src.danik.postservice.types.NotificationType;

@Data
@Builder
public class NotificationEvent {
    private Long eventId;
    private Long userId;
    private String text;
    private NotificationType notificationType;
}
