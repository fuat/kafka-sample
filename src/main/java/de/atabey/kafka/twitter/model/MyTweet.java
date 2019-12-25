/*
 * This document contains trade secret data which is the property of
 * Ippen Digital GmbH & Co. KG. Information contained herein may not be
 * used, copied or disclosed in whole or part except as permitted by
 * written agreement from Ippen Digital GmbH & Co. KG.
 *
 * Copyright (C) 2007-2019 Ippen Digital GmbH & Co. KG / Munich / Germany
 */
package de.atabey.kafka.twitter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "tweet")
@AllArgsConstructor
public class MyTweet {

    @Id
    private final long id;

    private String message;


}
