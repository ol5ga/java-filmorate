package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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


    @PostMapping
    public Film create(@RequestBody Film film) {
        if (validate(film)) {
            film.setId(++idGenerate);
            allFilms.add(film);
            log.info("Добавление пользователя");
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updateFilm){
        if (validate(updateFilm)) {
            for (Film film : allFilms) {
                if (film.getId() == updateFilm.getId()) {
                    allFilms.remove(film);
                    allFilms.add(updateFilm);
                } else{
                    throw new ValidationException("Такого фильма нет в базе");
                }
                log.info("Изменение пользователя");
            }
        }
        return updateFilm;
    }

    private boolean validate(Film film){
        if (film.getName() == null || film.getName().isBlank()){
            throw new ValidationException("Название не может быть пустым");
        } else if(film.getDescription().length()>200){
            throw new ValidationException("Описание не должно быть длинее 200 символов");
        } else if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))){
            throw new ValidationException("Дата релиза не верна");
        } else if(film.getDuration() <= 0){
            throw new ValidationException("Длительность фильма не может быть отрицательной");
        } else{
            return true;
        }
    }

}
