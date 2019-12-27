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
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String TOPIC_TWITTER_TWEETS = "twitter_tweets";
    public static final String TOPIC_RECORD_TWEETS = "record";

    @Autowired private KafkaProperties properties;
    @Autowired private ObjectProvider<RecordMessageConverter> messageConverter;

    @Bean
    public NewTopic tweetsTopic() {
        return TopicBuilder.name(TOPIC_TWITTER_TWEETS)
                .partitions(6)
                .replicas(1)
//                .config("min.insync.replicas", "2")
                .build();
    }

    @Bean
    public NewTopic recordsTopic() {
        return TopicBuilder.name(TOPIC_RECORD_TWEETS)
                .partitions(3)
                .replicas(1)
                .build();
    }


//    @Bean
//    public KafkaListenerContainerFactory myKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
//        factory.setConsumerFactory(consumerFactory());
//        factory.setConcurrency(4);
//        factory.setContainerCustomizer(containerCustomizer());
//        return factory;
//    }
//
//    @Bean
//    public ConsumerFactory consumerFactory() {
//        return new DefaultKafkaConsumerFactory<Integer, String>(kafkaProperties.buildConsumerProperties());
//    }

    @Bean
    public JsonMessageConverter jsonMessageConverter() {
        return new JsonMessageConverter();
    }

//    private ContainerCustomizer containerCustomizer() {
//        return new ContainerCustomizer() {
//            @Override
//            public void configure(AbstractMessageListenerContainer container) {
//            }
//        };
//    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> recordListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);
        factory.setErrorHandler(seekErrorHandler());
        return factory;
    }

    @Bean
    public ErrorHandler seekErrorHandler() {
        return new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate()));
    }


    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate() {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory());
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        kafkaTemplate.setProducerListener(kafkaProducerListener());
        kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }

    @Bean
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }

    @Bean
    public ProducerFactory<Object, Object> kafkaProducerFactory() {
        DefaultKafkaProducerFactory<Object, Object> factory = new DefaultKafkaProducerFactory<>(
                this.properties.buildProducerProperties());
        String transactionIdPrefix = this.properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return factory;
    }
}
