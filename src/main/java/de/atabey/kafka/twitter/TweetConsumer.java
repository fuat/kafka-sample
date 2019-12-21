package de.atabey.kafka.twitter;

import de.atabey.kafka.twitter.config.KafkaConfig;
import de.atabey.kafka.twitter.model.Tweet;
import de.atabey.kafka.twitter.model.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TweetConsumer {

    private final TweetRepository tweetRepository;

    @Autowired
    public TweetConsumer(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @KafkaListener(topics = {KafkaConfig.TOPIC_TWITTER_TWEETS})
    public void consumeTweets(Tweet tweet) {
        tweetRepository.save(tweet);
    }
}
