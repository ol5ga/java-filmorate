package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    public Map<Integer, Film> allFilms = new HashMap<>();


    @Override
    public Film createFilm(Film film) {
        allFilms.put(film.getId(), film);
        return film;
    }


    public void deleteFilm(Film film) {
        if (allFilms.containsKey(film.getId())) {
            allFilms.remove(film.getId());
        } else {
            throw new ChangeException("Такого фильма нет в базе");
        }
    }

    @Override
    public Film updateFilm(Film updateFilm) {
        if (allFilms.containsKey(updateFilm.getId())) {
            allFilms.put(updateFilm.getId(), updateFilm);
        } else {
            throw new ChangeException("Такого фильма нет в базе");

        }
        return updateFilm;
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(allFilms.values());
    }

    @Override
    public Film getFilm(int id) {
        if (!allFilms.containsKey(id)) {
            throw new ChangeException("Такого фильма нет в базе");
        }
        return allFilms.get(id);
    }


}
