package ru.kpfu.itis.khakov.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.khakov.entity.Tweet;
import ru.kpfu.itis.khakov.service.TweetService;
import twitter4j.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Rustam Khakov on 02.07.2017.
 * Класс для обновления данных из твиттера
 */
@Component
public class UpdateTweetsUtil {
    @Autowired
    private TweetService tweetService;

    @Scheduled(fixedDelay = 30 * 1000)
    public void update() {
        Thread updateThread = new Thread(() -> {
            if (TweetUtils.getTwitter()!= null) {
                try {
                    Twitter twitter = TweetUtils.getTwitter();
                    Query query = new Query("microservices");
                    QueryResult result = twitter.search(query);
                    Set<Tweet> tweets = new HashSet<>();
                    Tweet tweet;
                    for (Status status : result.getTweets()) {
                        tweet = new Tweet();
                        tweet.setId(status.getId());
                        tweet.setText(status.getText());
                        tweet.setDate(status.getCreatedAt());
                        tweet.setRetweetCount(status.getRetweetCount());
                        tweet.setFavoriteCount(status.getFavoriteCount());
                        tweet.setAuthor(status.getUser().getName());
                        tweets.add(tweet);
                    }
                    tweetService.updateTweets(tweets);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        });
        updateThread.run();
    }
}