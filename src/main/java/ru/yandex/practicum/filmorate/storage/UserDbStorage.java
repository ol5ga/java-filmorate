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
import java.util.stream.Collectors;

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

    public void addFriend(int id, int friendId) {
        if(getUser(id) != null && getUser(friendId) != null) {
            String sqlAp = "select user_id from friends where friend_id = ? AND status = ?";
            List<Integer> applicationId = jdbcTemplate.queryForList(sqlAp, Integer.class, id, "application");
            if (applicationId.contains(friendId)) {
                String sqlUpdate = "update friends set status = ? where user_id = ? and friend_id = ?";
                jdbcTemplate.update(sqlUpdate, "friends", friendId, id);
                String sqlAdd = "insert into friends (user_id, friend_id, status) values (?,?,?)";
                jdbcTemplate.update(sqlAdd, id, friendId, "friends");
                log.info("Образована дружба между пользователями {} и {}", id, friendId);
            } else {
                String sql = "insert into friends (user_id, friend_id, status) values (?,?,?)";
                jdbcTemplate.update(sql, id, friendId, "application");
                log.info("Пользователь {} оставил заявку в друзья пользователю {}", id, friendId);
            }
        } else {
            throw new ChangeException("Такого пользователя не существует");
        }
    }
    @Override
    public void deleteFriend(int id, int friendId) {
//        if (printFriends(id).contains(friendId) && printFriends(friendId).contains(id)) {
            String sqlDel = "delete from friends where user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sqlDel,id, friendId);
            String sqlUpdate = "update friends set status = ? where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlUpdate, "application", friendId, id);
//        } else {
//            throw new ChangeException("Неверные пользователи");
//        }
    }
    @Override
    public List<User> printFriends(int id) {
        String sql = "select friend_id from friends where user_id = ?"; // AND status = ?";
        List<Integer> friendId = jdbcTemplate.queryForList(sql,Integer.class,id); //,"friends");
        return friendId.stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }
    @Override
    public List<User> printCommonFriends(int id, int otherId) {
        String sqlId = "select friend_id from friends where user_id = ?";// AND status = ?";
        List<Integer> friendId = jdbcTemplate.queryForList(sqlId,Integer.class,id);
        String sqlOther = "select friend_id from friends where user_id = ?";
        List<Integer> friendOther = jdbcTemplate.queryForList(sqlOther,Integer.class,otherId);
        return friendId.stream()
                .filter(friendOther::contains)
                .map(id1 -> getUser(id1))
                .collect(Collectors.toList());
    }

}
