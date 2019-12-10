/*
 * This document contains trade secret data which is the property of
 * Ippen Digital GmbH & Co. KG. Information contained herein may not be
 * used, copied or disclosed in whole or part except as permitted by
 * written agreement from Ippen Digital GmbH & Co. KG.
 *
 * Copyright (C) 2007-2019 Ippen Digital GmbH & Co. KG / Munich / Germany
 */
package de.atabey.kafka.twitter;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic tweetsTopic() {
        return TopicBuilder.name("twitter_tweets")
                .partitions(6)
                .replicas(1)
                .config("min.insync.replicas", "2")
                .build();
    }
}
