package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
//    private int idGenerate = 0;
    private final FilmService filmService;


    @GetMapping()
    public List<Film> getAllFilms() {
        return new ArrayList<Film>(filmService.getAllFilms());
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
//        film.setId(++idGenerate);
        filmService.createFilm(film);
        log.info("Добавление фильма");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film updateFilm) {
        filmService.updateFilm(updateFilm);
        log.info("Изменение фильма");
        return updateFilm;
    }

    @GetMapping("/{id}")
    public Film getUser(@PathVariable int id) {
        return filmService.getUser(id);
    }
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId){
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId){
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> printTop(@RequestParam (defaultValue = "10") int count){
        return filmService.printTop(count);
    }

}