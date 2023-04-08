package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
@Qualifier
public class FilmDbStorage implements FilmStorage{

    public FilmDbStorage() {
        super();
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
