package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;

@Primary
@Component
@Slf4j
@RequiredArgsConstructor
public class PropertyDBStorage implements PropertyStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, this::rowGenre);
    }

    @Override
    public Genre getGenre(int id) {
        String sql = "select * from genre where genre_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            log.info("Найден жанр с идентификатором: {}", userRows.getString("genre_id"));
            return jdbcTemplate.queryForObject(sql, this::rowGenre, id);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new ChangeException("Такого жанра не существует");

        }
    }

    @Override
    public List<MPA> getAllMpa() {
        String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, this::rowMpa);
    }

    @Override
    public MPA getMpa(int id) {
        String sql = "select * from MPA where MPA_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            log.info("Найден рейтинг с идентификатором: {}", userRows.getString("MPA_id"));
            return jdbcTemplate.queryForObject(sql, this::rowMpa, id);
        } else {
            log.info("Жанр с рейтингом {} не найден.", id);
            throw new ChangeException("Такого рейтинга не существует");

        }
    }

    public void addLike(int id, int userId) {
        String sqlAddLike = "insert into likes (film_id,user_id)" + "values(?,?)";
        jdbcTemplate.update(sqlAddLike, id, userId);
    }

    public void deleteLike(int id, int userId) {
        String sql = "SELECT user_id = ? FROM likes";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, userId);
        if (userRows.next()) {
            log.info("Лайк пользователя {} фильму {} удален", userId, id);
            jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?", id,userId);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new ChangeException("Такого пользователя не существует");
        }
    }

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

    public Genre rowGenre(ResultSet rs, int i) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();

    }

    public MPA rowMpa(ResultSet rs, int i) throws SQLException {

        return MPA.builder()
                .id(rs.getInt("MPA_id"))
                .name(rs.getString("MPA_name"))
                .build();
    }
}
