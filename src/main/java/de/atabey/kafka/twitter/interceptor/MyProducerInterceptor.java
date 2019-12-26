package de.atabey.kafka.twitter.interceptor;

import de.atabey.kafka.twitter.model.MyTweet;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

@Slf4j
public class MyProducerInterceptor implements ProducerInterceptor<String, MyTweet> {

    public MyProducerInterceptor() {
        log.info("Initializing  my producer interceptor");
    }

    @Override
    public ProducerRecord<String, MyTweet> onSend(ProducerRecord<String, MyTweet> record) {
        log.debug("Intercepting record {}", record);
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        log.info("Acknowledgement of Topic {}, Partition {}, Offset {}", metadata.topic(), metadata.partition(), metadata.offset());
        if(exception != null) {
            log.error("Error with {}", metadata, exception);
        }
    }

    @Override
    public void close() {
        log.debug("My Interceptor : close has been called");
    }

    @Override
    public void configure(Map<String, ?> configs) {
        log.debug("My Interceptor : configure what you want {}", configs);
    }
}
