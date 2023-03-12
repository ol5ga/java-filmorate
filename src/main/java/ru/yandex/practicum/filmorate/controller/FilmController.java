package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int idGenerate = 0;
    private final FilmService filmService;
    private final InMemoryFilmStorage storage;

    @Autowired
    public FilmController(FilmService filmService,InMemoryFilmStorage storage){
        this.filmService= filmService;
        this.storage = storage;
    }

    @GetMapping()
    public List<Film> getAllFilms() {

        return new ArrayList<Film>(storage.allFilms.values());
    }


    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        film.setId(++idGenerate);
        storage.createFilm(film);
        log.info("Добавление пользователя");

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film updateFilm) {
        storage.updateFilm(updateFilm);
        log.info("Изменение пользователя");

        return updateFilm;
    }

    @PutMapping
    public void addLike(){
//    /films/{id}/like/{userId}
    }

    @DeleteMapping
    public void deleteLike(){
//    /films/{id}/like/{userId}
    }

    @GetMapping
    public void printTop(){
//    /films/popular?count={count}
    }
}