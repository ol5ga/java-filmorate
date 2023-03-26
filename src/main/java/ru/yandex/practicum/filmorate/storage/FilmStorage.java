package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {

    Film createFilm(Film film);

    void deleteFilm(Film film);

    Film updateFilm(Film updateFilm);

    ArrayList<Film> getAllFilms();

    Film getFilm(int id);
}
