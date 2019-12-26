package de.atabey.kafka.twitter.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.event.KafkaEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerEventListener {

    @EventListener
    public void handleKafkaEvent(KafkaEvent event) {
        log.debug(event.toString());
    }
}
