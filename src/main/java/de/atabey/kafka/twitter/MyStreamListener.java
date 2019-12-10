package de.atabey.kafka.twitter;

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
class MyStreamListener implements StreamListener {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    MyStreamListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void onTweet(Tweet tweet) {
        System.out.println(tweet.toString());
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send("twitter_tweets", Long.valueOf(tweet.getId()).toString(), tweet.getText());
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
