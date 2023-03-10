package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    public Film createFilm(Film film);

    public void deleteFilm(Film film);

    public Film updateFilm(Film updateFilm);
}
