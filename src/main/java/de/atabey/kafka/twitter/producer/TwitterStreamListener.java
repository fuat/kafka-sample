package de.atabey.kafka.twitter.producer;

import de.atabey.kafka.twitter.config.KafkaConfig;
import de.atabey.kafka.twitter.model.MyTweet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class TwitterStreamListener implements StreamListener {

    private final KafkaTemplate<String, MyTweet> kafkaTemplate;

    @Autowired
    TwitterStreamListener(KafkaTemplate<String, MyTweet> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void onTweet(Tweet tweet) {
        log.info(tweet.toString());
        ListenableFuture<SendResult<String, MyTweet>> send = kafkaTemplate.send(KafkaConfig.TOPIC_TWITTER_TWEETS, Long.valueOf(tweet.getId()).toString(), mapTweet(tweet));
        send.addCallback(new ListenableFutureCallback<SendResult<String, MyTweet>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("something happended here", ex);
            }

            @Override
            public void onSuccess(SendResult<String, MyTweet> result) {
                log.info("Partition :{}, Offset : {}", result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }

    private MyTweet mapTweet(Tweet tweet) {
        return new MyTweet(tweet.getId(), tweet.getText());
    }

    @Override
    public void onDelete(StreamDeleteEvent deleteEvent) {
        System.out.println(deleteEvent.toString());
    }

    @Override
    public void onLimit(int numberOfLimitedTweets) {
        System.out.println(numberOfLimitedTweets);
    }

    @Override
    public void onWarning(StreamWarningEvent warningEvent) {
        System.out.println(warningEvent.toString());
    }
}
