package de.atabey.kafka.twitter.producer;

import de.atabey.kafka.twitter.config.KafkaConfig;
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

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    TwitterStreamListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void onTweet(Tweet tweet) {
        log.info(tweet.toString());
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(KafkaConfig.TOPIC_TWITTER_TWEETS, Long.valueOf(tweet.getId()).toString(), tweet.getText());
        send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("something happended here", ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Partition :{}, Offset : {}", result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
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
