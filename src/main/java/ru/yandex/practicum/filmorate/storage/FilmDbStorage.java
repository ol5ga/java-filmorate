package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
//@Qualifier
@Primary
@Component
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        return null;
    }

    @Override
    public void deleteFilm(Film film) {

    }

    @Override
    public Film updateFilm(Film updateFilm) {
        return null;
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return null;
    }

    @Override
    public Film getFilm(int id) {
        return null;
    }
}
