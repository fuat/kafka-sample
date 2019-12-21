/*
 * This document contains trade secret data which is the property of
 * Ippen Digital GmbH & Co. KG. Information contained herein may not be
 * used, copied or disclosed in whole or part except as permitted by
 * written agreement from Ippen Digital GmbH & Co. KG.
 *
 * Copyright (C) 2007-2019 Ippen Digital GmbH & Co. KG / Munich / Germany
 */
package de.atabey.kafka.twitter.config;


import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories("de.atabey.kafka.twitter.model")
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient elasticSearchClient() {
        return RestClients.create(ClientConfiguration.builder()
                .connectedTo("twitter-5658650936.eu-central-1.bonsaisearch.net:443")
                .usingSsl()
                .withBasicAuth("mi8ur2muzc", "ud91kz1ktb")
                .build()
        ).rest();

    }
}
