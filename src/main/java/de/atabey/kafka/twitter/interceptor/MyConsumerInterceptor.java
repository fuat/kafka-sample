package de.atabey.kafka.twitter.interceptor;

import de.atabey.kafka.twitter.model.MyTweet;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

@Slf4j
public class MyConsumerInterceptor implements ConsumerInterceptor<String, MyTweet> {

    public MyConsumerInterceptor() {
        log.info("initializing");
    }

    @Override
    public ConsumerRecords<String, MyTweet> onConsume(ConsumerRecords<String, MyTweet> records) {
        records.partitions().forEach(topicPartition -> {
            log.info("Consuming from Topic {} and Partition {} following number of records: {}", topicPartition.topic(), topicPartition.partition(), records.records(topicPartition).size());
        });
        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        offsets.forEach((topicPartition, offsetAndMetadata) -> {
            log.info("Consumer commits Topic {}, Partition {} and offset {}", topicPartition.topic(), topicPartition.partition(), offsetAndMetadata.offset());
        });

    }

    @Override
    public void close() {
        log.info("Calling close ");
    }

    @Override
    public void configure(Map<String, ?> configs) {
        log.info("Calling configure ");

    }
}
