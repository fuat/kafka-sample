/*
 * This document contains trade secret data which is the property of
 * Ippen Digital GmbH & Co. KG. Information contained herein may not be
 * used, copied or disclosed in whole or part except as permitted by
 * written agreement from Ippen Digital GmbH & Co. KG.
 *
 * Copyright (C) 2007-2019 Ippen Digital GmbH & Co. KG / Munich / Germany
 */
package de.atabey.kafka.twitter;

import de.atabey.kafka.twitter.producer.TwitterStreamListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TwitterStreamRunner implements ApplicationRunner {

    private final Twitter twitter;
    private final TwitterStreamListener twitterStreamListener;

    @Autowired
    public TwitterStreamRunner(Twitter twitter, TwitterStreamListener twitterStreamListener) {
        this.twitter = twitter;
        this.twitterStreamListener = twitterStreamListener;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        twitter.streamingOperations().filter("erdogan", Arrays.asList(twitterStreamListener));
    }

}
