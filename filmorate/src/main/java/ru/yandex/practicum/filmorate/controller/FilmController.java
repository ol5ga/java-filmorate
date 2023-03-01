package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    int idGenerate = 0;
    private List<Film> allFilms = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping()
    public List<Film> getAllFilms() {
        return allFilms;
    }

    @GetMapping("/film")
    public Film getFilm() {
        return new Film("Судьба", "Фильм отяжелой судьбе", LocalDate.of(2015,2,1), 85L);
    }

    @PostMapping(value = "/film")
    public Film create(@RequestBody Film film) {
        film.setId(++idGenerate);
        allFilms.add(film);
        log.info("Добавление пользователя");
        return film;

    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updateFilm){
        for (Film film : allFilms) {
            if (film.getId() == updateFilm.getId()){
                allFilms.remove(film);
                allFilms.add(updateFilm);
            }
            log.info("Изменение пользователя");
        }
        return updateFilm;
    }
}
