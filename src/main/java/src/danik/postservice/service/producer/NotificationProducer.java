package src.danik.postservice.service.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import src.danik.postservice.kafka.event.NotificationEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    @Value("${kafka.topic.notification}")
    private String topic;

    public void sendNotificationEvent(NotificationEvent notificationEvent) {
        log.info("Sending notification event: {}", notificationEvent);
        kafkaTemplate.send(topic, notificationEvent);
    }
}
