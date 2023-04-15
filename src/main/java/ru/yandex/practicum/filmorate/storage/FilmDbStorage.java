package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Primary
@Component
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

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
        String sql = "insert into film_genre (film_id,genre_id)" + "values(?,?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film updateFilm) {
        String sqlQuery = "update public.films set name = ?, description =?, release_data = ?, duration = ?, MPA = ? where film_id =?";
        jdbcTemplate.update(sqlQuery, updateFilm.getName(), updateFilm.getDescription(), updateFilm.getReleaseDate(), updateFilm.getDuration(), updateFilm.getMpa().getId(), updateFilm.getId());
        String sqlDelGenre = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlDelGenre, updateFilm.getId());
        for (Genre genre : updateFilm.getGenres()) {
            String sqlPutGenre = "insert into film_genre (film_id,genre_id)" + "values(?,?)";
            jdbcTemplate.update(sqlPutGenre, updateFilm.getId(), genre.getId());
        }
        return getFilm(updateFilm.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select * from public.films f join MPA m ON f.MPA = m.MPA_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getFilm(int id) {
        String sql = "select * from films f join MPA m ON f.MPA = m.MPA_id where film_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            log.info("Найден фильм: {} {}", userRows.getString("film_id"), userRows.getString("name"));
            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new ChangeException("Такого фильма не существует");

        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int i) throws SQLException {
        MPA mpa = rowMpa(resultSet, i);
        String sql = "select * from film_genre fg join genre g ON fg.genre_id = g.genre_id where film_id = ? ";
        List<Genre> genreList = jdbcTemplate.query(sql, (rs, i1) -> rowGenre(rs, i1), resultSet.getInt("film_id"));
        Set<Genre> genres = new HashSet<>(genreList);
        String sqlLikes = "select count(user_id) from likes where film_id = ?";
        List<Integer> likesList = jdbcTemplate.queryForList(sqlLikes, Integer.class, resultSet.getInt("film_id"));
        int likes = likesList.get(0);
        Film film = new Film(
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_data").toLocalDate(),
                resultSet.getInt("duration"),
                likes,
                mpa,
                genres

        );
        film.setId(resultSet.getInt("film_id"));
        return film;
    }

    private MPA rowMpa(ResultSet rs, int i) throws SQLException {

        return MPA.builder()
                .id(rs.getInt("MPA_id"))
                .name(rs.getString("MPA_name"))
                .build();
    }

    private Genre rowGenre(ResultSet rs, int i) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();

    }


}
