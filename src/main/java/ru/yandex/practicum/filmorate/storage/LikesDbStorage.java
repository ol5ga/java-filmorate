package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesStorage;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;

@Primary
@Component
@Slf4j
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;
    @Override
    public void addLike(int id, int userId) {
        String sqlAddLike = "insert into likes (film_id,user_id)" + "values(?,?)";
        jdbcTemplate.update(sqlAddLike, id, userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        String sql = "SELECT user_id = ? FROM likes";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, userId);
        if (userRows.next()) {
            log.info("Лайк пользователя {} фильму {} удален", userId, id);
            jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?", id,userId);
        } else {
            log.warn("Пользователь с идентификатором {} не найден.", userId);
            throw new ChangeException("Такого пользователя не существует");
        }
    }

    @Override
    public List<Film> printTop(int count) {
        List<Film> films = filmStorage.getAllFilms();
        if (count > films.size()) {
            count = films.size();
        }
        return films.stream()
                .sorted((p0, p1) -> {
                    int comp = compare(p0.getRate(), p1.getRate());
                    return -1 * comp;
                }).limit(count)
                .collect(Collectors.toList());

    }
}
