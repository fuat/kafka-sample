/*
 * This document contains trade secret data which is the property of
 * Ippen Digital GmbH & Co. KG. Information contained herein may not be
 * used, copied or disclosed in whole or part except as permitted by
 * written agreement from Ippen Digital GmbH & Co. KG.
 *
 * Copyright (C) 2007-2019 Ippen Digital GmbH & Co. KG / Munich / Germany
 */
package de.atabey.kafka.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TwitterStreamListener implements ApplicationRunner {

    private final Twitter twitter;
    private final MyStreamListener myStreamListener;

    @Autowired
    public TwitterStreamListener(Twitter twitter, MyStreamListener myStreamListener) {
        this.twitter = twitter;
        this.myStreamListener = myStreamListener;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        twitter.streamingOperations().filter("kafka", List.of(myStreamListener));
    }

}
