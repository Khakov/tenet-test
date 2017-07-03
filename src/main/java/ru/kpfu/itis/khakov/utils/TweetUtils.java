package ru.kpfu.itis.khakov.utils;

import ru.kpfu.itis.khakov.entity.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Rustam Khakov on 02.07.2017.
 *         вспомогательный класс для работы с твиттером
 */
public class TweetUtils {
    private final static String CONSUMER_PUBLIC = "If21I63ISmYa35SFGKw2JH7Ha";
    private final static String CONSUMER_SECRET = "Yr7bKqr9DaOMUyg9yRqqGxLVdjfJOOAAVdSaMAQjv1AUr4Dkgi";
    private static volatile Twitter twitter = null;
    private static volatile Twitter createTwitter = null;

    /**
     * синхронизирует данные
     *
     * @param databaseTweets данные из таблицы
     * @param apiTweets      пришедшие данные
     * @return данные которые нужно вставить в таблицу
     */
    public static List<Tweet> getInsertTweets(Set<Tweet> databaseTweets, Set<Tweet> apiTweets) {
        List<Tweet> tweets = new LinkedList<>();
        for (Tweet tweet : apiTweets) {
            if (!databaseTweets.contains(tweet)) {
                tweets.add(tweet);
            }
        }
        return tweets;
    }

    /**
     * синхронизирует данные
     *
     * @param databaseTweets данные из таблицы
     * @param apiTweets      пришедшие данные
     * @return данные которые нужно обновить
     */
    public static List<Tweet> getUpdateTweets(Set<Tweet> databaseTweets, Set<Tweet> apiTweets) {
        List<Tweet> tweets = new LinkedList<>();
        for (Tweet tweet : apiTweets) {
            if (databaseTweets.contains(tweet)) {
                for (Tweet updateTweet : databaseTweets) {
                    if (updateTweet.equals(tweet) &&
                            !(updateTweet.getText().equals(tweet.getText()) &&
                                    updateTweet.getFavoriteCount() == tweet.getFavoriteCount() &&
                                    updateTweet.getRetweetCount() == tweet.getRetweetCount())) {
                        tweets.add(tweet);
                    }
                }
            }
        }
        return tweets;
    }

    synchronized public static Twitter getTwitter() {
        return twitter;
    }

    public static boolean setTwitter(Twitter currTwitter) {
        try {
            synchronized (Twitter.class) {
                twitter = currTwitter.getOAuthAccessToken() == null ? null : currTwitter;
                return currTwitter.getOAuthAccessToken().getUserId() != 0;
            }
        } catch (TwitterException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Создает новый экземпляр Твиттера, исходя из настроек
     *
     * @return созданный Твиттер
     */
    public static Twitter createTwitter() {
        if (createTwitter == null) {
            createTwitter = TwitterFactory.getSingleton();
            createTwitter.setOAuthConsumer(CONSUMER_PUBLIC, CONSUMER_SECRET);
        }
        return createTwitter;
    }
}
