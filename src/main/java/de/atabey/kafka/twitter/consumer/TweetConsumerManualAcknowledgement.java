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

//@Component
@Slf4j
public class TweetConsumerManualAcknowledgement {

    public static final String GROUP_ID = "tweetApp";
    private final TweetRepository tweetRepository;
    private final Random random = new Random();

    @Autowired
    public TweetConsumerManualAcknowledgement(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    //    spring.kafka.listener.ack-mode=manual
//    @KafkaListener(topics = {KafkaConfig.TOPIC_TWITTER_TWEETS}, groupId = GROUP_ID)
    public void consumeTweet(@Payload MyTweet myTweet, Acknowledgment acknowledgment) {
        tweetRepository.findById(myTweet.getId()).ifPresent(tweet -> log.warn("Tweet with id {} exists already", tweet.getId()));
        if (random.nextInt() % 3 == 0) {
            log.error("Consumer is throwing exception with tweet {}", myTweet);
            acknowledgment.nack(10l);
            throw new RuntimeException("Consumer is throwing exception with tweet " + myTweet.getId());
        }
        tweetRepository.save(myTweet);
        acknowledgment.acknowledge();
    }

//    @KafkaListener(topics = {KafkaConfig.TOPIC_TWITTER_TWEETS}, groupId = GROUP_ID)
    public void consumeTweetsInBatch(List<Message<MyTweet>> messageList, Acknowledgment acknowledgment) {
        Iterator<Message<MyTweet>> iterator = messageList.iterator();
        while (iterator.hasNext()) {
            final MyTweet myTweet = (MyTweet) iterator.next();
            tweetRepository.findById(myTweet.getId()).ifPresent(tweet -> log.warn("Tweet with id {} exists already", tweet.getId()));
            if (random.nextInt() % 3 == 0) {
                log.error("Consumer is throwing exception with tweet {}", myTweet);
                acknowledgment.nack(10l);
                throw new RuntimeException("Consumer is throwing exception with tweet " + myTweet.getId());
            }
            tweetRepository.save(myTweet);
        }
        acknowledgment.acknowledge();

    }
}
