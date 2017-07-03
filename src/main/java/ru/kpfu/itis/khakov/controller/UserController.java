package ru.kpfu.itis.khakov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kpfu.itis.khakov.entity.User;
import ru.kpfu.itis.khakov.service.UserService;
import ru.kpfu.itis.khakov.utils.TweetUtils;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rustam Khakov on 02.07.2017.
 * контроллер для авторизации и аутентификации
 */
@Controller
public class UserController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    @Qualifier("currentUser")
    private User user;
    @Autowired
    UserService userService;

    @GetMapping("/hi")
    public String getAccessToken() {
        Twitter twitter;
        if (request.getSession().getAttribute("twitter") == null) {
            twitter = TweetUtils.createTwitter();
            request.getSession().setAttribute("twitter", twitter);
        } else {
            twitter = (Twitter) request.getSession().getAttribute("twitter");
        }
        if (user.getAccessToken() == null) {
            RequestToken requestToken = null;
            try {
                StringBuffer callbackURL = request.getRequestURL();
                int index = callbackURL.lastIndexOf("/");
                callbackURL.replace(index, callbackURL.length(), "").append("/callback");
                if ( request.getSession().getAttribute("requestToken")!= null){
                    requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
                }else {
                    requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
                    request.getSession().setAttribute("requestToken", requestToken);
                }
                return "redirect:" + requestToken.getAuthenticationURL();
            } catch (TwitterException e) {
                e.printStackTrace();
                return "error";
            }
        } else {
            if (TweetUtils.getTwitter() == null) {
                twitter.setOAuthAccessToken(new AccessToken(user.getAccessToken(), user.getSecretAccessToken()));
                request.getSession().setAttribute("twitter", twitter);
                TweetUtils.setTwitter(twitter);
            }
            return "redirect:/main";
        }
    }

    @GetMapping("/callback")
    public String callback() {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        try {
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
            twitter.setOAuthAccessToken(accessToken);
            TweetUtils.setTwitter(twitter);
            user.setAccessToken(accessToken.getToken());
            userService.insertUser(user, accessToken);
            request.getSession().setAttribute("twitter", twitter);
            request.getSession().removeAttribute("requestToken");
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return "redirect:/main";
    }
}
