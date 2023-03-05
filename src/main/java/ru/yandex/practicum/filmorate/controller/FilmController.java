package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

    @GetMapping()
    public List<Film> getAllFilms() {

        return new ArrayList<Film>(allFilms.values());
    }


    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        if (validate(film)) {
            film.setId(++idGenerate);
            allFilms.put(film.getId(),film);
            log.info("Добавление пользователя");
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film updateFilm){
        if (validate(updateFilm)) {

                if (allFilms.containsKey(updateFilm.getId())) {
                    allFilms.remove(updateFilm.getId());
                    allFilms.put(updateFilm.getId(),updateFilm);
                } else{
                    throw new ChangeException("Такого фильма нет в базе");

                }
                log.info("Изменение пользователя");
        }
        return updateFilm;
    }

    private boolean validate(Film film){
//        if (film.getName() == null || film.getName().isBlank()){
//            throw new ValidationException("Название не может быть пустым");
//        } else if(film.getDescription().length()>200){
//            throw new ValidationException("Описание не должно быть длинее 200 символов");
//        } else
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))){
            throw new ValidationException("Дата релиза не верна");
//        } else if(film.getDuration() <= 0){
//            throw new ValidationException("Длительность фильма не может быть отрицательной");
        } else{
            return true;
        }
    }

}