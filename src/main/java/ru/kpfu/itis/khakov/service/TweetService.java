package ru.kpfu.itis.khakov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.khakov.entity.Tweet;
import ru.kpfu.itis.khakov.repository.TweetRepository;
import ru.kpfu.itis.khakov.utils.TweetUtils;

import java.util.List;
import java.util.Set;

/**
 * @author Rustam Khakov on 02.07.2017.
 */
@Service
public class TweetService {
    @Autowired
    TweetRepository tweetRepository;

    public void updateTweets(Set<Tweet> tweets) {
        Set<Tweet> databaseTweets = getAllTweets();
        tweetRepository.updateTweets(TweetUtils.getUpdateTweets(databaseTweets, tweets));
        insertTweets(TweetUtils.getInsertTweets(databaseTweets, tweets));
    }

    public void insertTweets(List<Tweet> tweets) {
        tweetRepository.insertTweets(tweets);
    }

    public Set<Tweet> getAllTweets() {
        return tweetRepository.allTweets();
    }

    public Set<Tweet> searchByString(String string) {
        return tweetRepository.searchByString("%" + string + "%");
    }
    public Set<Tweet> selectAllOrderBy(String columnName, String orderType){
        return tweetRepository.selectAllOrderBy(columnName, orderType);
    }
}
