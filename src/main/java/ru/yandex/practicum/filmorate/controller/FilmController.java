package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int idGenerate = 0;
    private Map<Integer, Film> allFilms = new HashMap<>();
    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping()
    public List<Film> getAllFilms() {

        return new ArrayList<Film>(allFilms.values());
    }


    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        validate(film);
        film.setId(++idGenerate);
        allFilms.put(film.getId(), film);
        log.info("Добавление пользователя");

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film updateFilm) {
        validate(updateFilm);

        if (allFilms.containsKey(updateFilm.getId())) {
            allFilms.put(updateFilm.getId(), updateFilm);
        } else {
            throw new ChangeException("Такого фильма нет в базе");

        }
        log.info("Изменение пользователя");

        return updateFilm;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не верна");
        }
    }

}