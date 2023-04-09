package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.exceptions.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Component
@Slf4j
public class UserDbStorage implements UserStorage{

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into public.users (email, login, name, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setObject(4, user.getBirthday());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User updateUser) {
        String sqlQuery = "update public.users set email = ?, login =?, name = ?, birthday = ? where user_id =?";
        jdbcTemplate.update(sqlQuery, updateUser.getEmail(),updateUser.getLogin(),updateUser.getName(),updateUser.getBirthday(),updateUser.getId());
        return getUser(updateUser.getId());

    }

    @Override
    public List<User> getAllUser() {
        String sqlQuery = "select * from public.users";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        return users;
    }

    private User mapRowToUser(ResultSet resultSet, int i) throws SQLException {
        User user = new User(
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate ("birthday").toLocalDate()
        );
        user.setId(resultSet.getInt("user_id"));
        return user;
    }

    @Override
    public User getUser(int id) {
        String sql = "select * from users where user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql,id);
        if(userRows.next()) {
            log.info("Найден пользователь: {} {}", userRows.getString("user_id"), userRows.getString("login"));
         return    jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } else {
        log.info("Пользователь с идентификатором {} не найден.", id);
            throw new ChangeException("Такого пользователя не существует");

    }
    }

}
