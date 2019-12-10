package de.atabey.kafka.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TwitterRestApi {

    private final Twitter twitter;

    @Autowired
    public TwitterRestApi(Twitter twitter) {
        this.twitter = twitter;
    }


    @GetMapping("profile")
    public TwitterProfile twitterProfile() {
        return twitter.userOperations().getUserProfile();
    }

}
