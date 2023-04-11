package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//@Qualifier
@Primary
@Component
@Slf4j
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into public.films (name, description, release_data, duration, MPA) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3, film.getReleaseDate());
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film updateFilm) {
        String sqlQuery = "update public.films set name = ?, description =?, release_data = ?, duration = ?, MPA = ? where film_id =?";
        jdbcTemplate.update(sqlQuery, updateFilm.getName(),updateFilm.getDescription(),updateFilm.getReleaseDate(),updateFilm.getDuration(),updateFilm.getMpa(), updateFilm.getId());
        return getFilm(updateFilm.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select * from public.films";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        return films;
    }

    @Override
    public Film getFilm(int id) {
        String sql = "select * from films where film_id = ? ";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql,id);
        if(userRows.next()) {
            log.info("Найден фильм: {} {}", userRows.getString("film_id"), userRows.getString("name"));
            return    jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new ChangeException("Такого фильма не существует");

        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int i) throws SQLException {
        Film film = new Film(
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_data").toLocalDate(),
                resultSet.getInt("duration")
        );
        film.setId(resultSet.getInt("film_id"));
        return film;
    }
}
