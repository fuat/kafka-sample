package de.atabey.kafka.twitter.consumer;

import de.atabey.kafka.twitter.config.KafkaConfig;
import de.atabey.kafka.twitter.model.MyTweet;
import de.atabey.kafka.twitter.model.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class TweetConsumer {

    private final TweetRepository tweetRepository;

    @Autowired
    public TweetConsumer(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @KafkaListener(topics = {KafkaConfig.TOPIC_TWITTER_TWEETS})
    public void consumeTweets(@Payload MyTweet myTweet) {
        tweetRepository.save(myTweet);
    }
}
