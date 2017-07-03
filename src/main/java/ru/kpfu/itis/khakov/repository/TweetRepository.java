package ru.kpfu.itis.khakov.repository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.khakov.entity.Tweet;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Rustam Khakov on 01.07.2017.
 */
@Component
public class TweetRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static Logger logger = Logger.getLogger(TweetRepository.class);
    public Set<Tweet> allTweets() {
        String sql = "SELECT * FROM posts";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        return getMapResult(rows);
    }

    public void insertTweets(List<Tweet> tweets) {
        if (tweets.size() > 0) {
            jdbcTemplate.batchUpdate("INSERT INTO posts VALUES (?, ?, ?, ?,?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            Tweet tweet = tweets.get(i);
                            ps.setLong(1, tweet.getId());
                            ps.setString(2, tweet.getAuthor());
                            ps.setString(3, tweet.getText());
                            ps.setLong(4, tweet.getDate().getTime());
                            ps.setInt(5, tweet.getRetweetCount());
                            ps.setInt(6, tweet.getFavoriteCount());
                        }

                        @Override
                        public int getBatchSize() {
                            return tweets.size();
                        }
                    });
        logger.info(new Date() + " " + tweets.size() + " записано");
        }
    }

    public void updateTweets(List<Tweet> tweets) {
        if (tweets.size() > 0) {
            jdbcTemplate.batchUpdate("UPDATE posts SET author=?, text=?, date=?, " +
                            "retweet_count=?, favorite_count=? WHERE id = ?",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            Tweet tweet = tweets.get(i);
                            ps.setString(1, tweet.getAuthor());
                            ps.setString(2, tweet.getText());
                            ps.setLong(3, tweet.getDate().getTime());
                            ps.setInt(4, tweet.getRetweetCount());
                            ps.setInt(5, tweet.getFavoriteCount());
                            ps.setLong(6, tweet.getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return tweets.size();
                        }
                    });
            logger.info(new Date() + " " + tweets.size() + " записей обновлено");
        }
    }

    public Set<Tweet> searchByString(String string) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT * FROM posts WHERE text LIKE ? OR author LIKE ?",
                new Object[]{string,string});
        return getMapResult(rows);
    }
    public Set<Tweet> selectAllOrderBy(String columnName, String orderType) {
        StringBuffer sql= new StringBuffer("SELECT * FROM posts ORDER BY ")
                .append(columnName).append(" ")
                .append(orderType);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql.toString());
        return getMapResult(rows);
    }
    private Set<Tweet> getMapResult(List<Map<String,Object>> rows){
        Set<Tweet> tweets = new HashSet<>();
        Tweet tweet;
        for (Map row : rows) {
            tweet = new Tweet();
            tweet.setText((String) row.get("text"));
            tweet.setId((Long) (row.get("id")));
            tweet.setAuthor((String) row.get("author"));
            tweet.setFavoriteCount((Integer) row.get("favorite_count"));
            tweet.setRetweetCount((Integer) row.get("retweet_count"));
            tweet.setDate(new Date((Long) row.get("date")));
            tweets.add(tweet);
        }
        return tweets;
    }
}