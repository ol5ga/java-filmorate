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
@Primary
@Component
@Slf4j
@RequiredArgsConstructor
public class PropertyDBStorage implements PropertyStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "select * from genre";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::rowGenre);
        return genres;
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
        List<MPA> Mpas = jdbcTemplate.query(sqlQuery, this::rowMpa);
        return Mpas;
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
