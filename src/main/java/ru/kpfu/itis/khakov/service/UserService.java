package ru.kpfu.itis.khakov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.khakov.entity.User;
import ru.kpfu.itis.khakov.repository.UserRepository;
import twitter4j.auth.AccessToken;

/**
 * @author Rustam Khakov on 02.07.2017.
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void insertUser(User user, AccessToken accessToken) {
        user.setId(accessToken.getUserId());
        user.setAccessToken(accessToken.getToken());
        user.setSecretAccessToken(accessToken.getTokenSecret());
        userRepository.insertUser(user);
    }
}
