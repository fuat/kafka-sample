/*
 * This document contains trade secret data which is the property of
 * Ippen Digital GmbH & Co. KG. Information contained herein may not be
 * used, copied or disclosed in whole or part except as permitted by
 * written agreement from Ippen Digital GmbH & Co. KG.
 *
 * Copyright (C) 2007-2019 Ippen Digital GmbH & Co. KG / Munich / Germany
 */
package de.atabey.kafka.twitter.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String TOPIC_TWITTER_TWEETS = "twitter_tweets";

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public NewTopic tweetsTopic() {
        return TopicBuilder.name(TOPIC_TWITTER_TWEETS)
                .partitions(6)
                .replicas(1)
//                .config("min.insync.replicas", "2")
                .build();
    }


//    @Bean
//    public KafkaListenerContainerFactory myKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
//        factory.setConsumerFactory(consumerFactory());
//        factory.setConcurrency(4);
//        return factory;
//    }
//
//    @Bean
//    public ConsumerFactory consumerFactory() {
//        return new DefaultKafkaConsumerFactory<Integer, String>(kafkaProperties.buildConsumerProperties());
//    }
}
