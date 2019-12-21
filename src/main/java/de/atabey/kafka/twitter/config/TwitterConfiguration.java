package de.atabey.kafka.twitter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class TwitterConfiguration  {

    @Bean
    public TwitterTemplate twitterTemplate(Environment environment) {
        return new TwitterTemplate(
                environment.getProperty("twitter.consumer.key"),
                environment.getProperty("twitter.consumer.secret"),
                environment.getProperty("twitter.access.token"),
                environment.getProperty("twitter.access.token.secret")
                );
    }
}
