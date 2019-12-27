package de.atabey.kafka.twitter.consumer;

import de.atabey.kafka.twitter.config.KafkaConfig;
import de.atabey.kafka.twitter.model.MyTweet;
import de.atabey.kafka.twitter.model.TweetRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

//@Component
@Slf4j
public class ConsumerConsistencyChecker extends AbstractConsumerSeekAware {

    private static final String GROUP_ID = "tweetChecker";

    private final TweetRepository tweetRepository;
    private static int existing = 0;
    private static int missing = 0;

    @Autowired
    public ConsumerConsistencyChecker(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @KafkaListener(topics = {KafkaConfig.TOPIC_RECORD_TWEETS}, groupId = GROUP_ID,
            properties = {"auto.offset.reset=earliest"})
    public void checkConsistency(@Payload MyTweet myTweet, Acknowledgment acknowledgment) {
        Optional<MyTweet> tweet = tweetRepository.findById(myTweet.getId());
        if(tweet.isPresent()) {
            log.info("Tweet with id {} exists", myTweet.getId());
            existing++;
        } else {
            log.info("Tweet with id {} missing", myTweet.getId());
            missing++;
        }
        log.info("Total: {}, Existing: {}, Missing: {}", existing+missing, existing, missing);
        acknowledgment.acknowledge();
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        callback.seekToBeginning(assignments.keySet());
    }
}
