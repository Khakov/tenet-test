package ru.kpfu.itis.khakov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kpfu.itis.khakov.entity.Tweet;
import ru.kpfu.itis.khakov.service.TweetService;
import twitter4j.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rustam Khakov on 01.07.2017.
 * Основной контроллер
 */
@Controller
public class TwitterController {
    @Autowired
    TweetService tweetService;
    @Autowired
    HttpServletRequest request;

    @GetMapping("/")
    public String getMainPage(ModelMap map) throws TwitterException {
        // The factory instance is re-useable and thread safe.
        if (request.getSession().getAttribute("twitter") == null) {
            return "redirect:/hi";
        } else {
            map.put("tweets", tweetService.getAllTweets());
            return "main";
        }
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "error";
    }

    @GetMapping("/search")
    @ResponseBody
    Set<Tweet> getTweets(@RequestParam("q") String q) {
        Set<Tweet> tweets = new HashSet<>();
        if (q.startsWith("2")) {
            tweets = tweetService.searchByString(q.substring(1));
        } else {
            String orderType = "DESC";
            if (q.contains("0")) {
                orderType = "ASC";
            }
            if (q.startsWith("id")) tweets = tweetService.selectAllOrderBy("id", orderType);
            if (q.startsWith("au")) tweets = tweetService.selectAllOrderBy("author", orderType);
            if (q.startsWith("da")) tweets = tweetService.selectAllOrderBy("date", orderType);
            if (q.startsWith("te")) tweets = tweetService.selectAllOrderBy("text", orderType);
            if (q.startsWith("fa")) tweets = tweetService.selectAllOrderBy("favorite_count", orderType);
            if (q.startsWith("re")) tweets = tweetService.selectAllOrderBy("retweet_count", orderType);
        }
        return tweets;
    }
}
