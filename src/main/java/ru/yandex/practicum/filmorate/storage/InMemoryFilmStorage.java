package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private int idGenerate = 0;
    private Map<Integer, Film> allFilms = new HashMap<>();
    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);


    @Override
    public Film createFilm(Film film) {
        validate(film);
        film.setId(++idGenerate);
        return film;

    }
    @Override
    public void deleteFilm(Film film) {
        allFilms.remove(film.getId());

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

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не верна");
        }
    }
}
