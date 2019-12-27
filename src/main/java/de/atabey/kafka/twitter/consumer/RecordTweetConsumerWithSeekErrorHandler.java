package de.atabey.kafka.twitter.consumer;

import de.atabey.kafka.twitter.config.KafkaConfig;
import de.atabey.kafka.twitter.model.MyTweet;
import de.atabey.kafka.twitter.model.TweetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class RecordTweetConsumerWithSeekErrorHandler {

    public static final String GROUP_ID = "record-consumer";
    private final TweetRepository tweetRepository;
    private final Random random = new Random();

    @Autowired
    public RecordTweetConsumerWithSeekErrorHandler(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @KafkaListener(topics = {KafkaConfig.TOPIC_RECORD_TWEETS}, groupId = GROUP_ID, containerFactory = "recordListenerContainerFactory")
    public void consumeTweet(@Payload MyTweet myTweet) {
        tweetRepository.findById(myTweet.getId()).ifPresent(tweet -> log.warn("Tweet with id {} exists already", tweet.getId()));
        if (random.nextInt() % 3 == 0) {
            log.error("Consumer is throwing exception with tweet {}", myTweet);
            throw new IllegalStateException("Consumer is throwing exception with tweet " + myTweet.getId());
        }
        tweetRepository.save(myTweet);
        if (random.nextInt() % 7 == 0) {
            log.error("Consumer is throwing exception with tweet {} after save", myTweet);
            throw new IllegalStateException("Consumer is throwing exception with tweet " + myTweet.getId());
        }
    }

}
