package ru.kpfu.itis.khakov.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.khakov.entity.User;

/**
 * @author Rustam Khakov on 01.07.2017.
 */
@Component
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BCryptPasswordEncoder bCrypt;

    public User getUserByLoginAndPassword(String login, String password) {
        User user;
        try {
            user = (User) jdbcTemplate.queryForObject(
                    "SELECT * FROM users where username = ?", new Object[]{login},
                    new BeanPropertyRowMapper(User.class));
        } catch (EmptyResultDataAccessException e) {
            user = null;
        }
        if (user != null && bCrypt.matches(password, user.getPassword())) {
            return user;
        } else {
            user = new User();
            user.setUsername(login);
            user.setPassword(bCrypt.encode(password));
            return user;
        }
    }

    public User insertUser(User user) {
        jdbcTemplate.update("INSERT INTO users VALUES(?,?,?,?,?)",
                new Object[]{user.getId(),user.getUsername(), user.getPassword(),
                        user.getAccessToken(), user.getSecretAccessToken()});
        return user;
    }
}
