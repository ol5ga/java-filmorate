package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.PropertyService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService service;

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return new ArrayList<Genre>(service.getAllGenres());
    }


    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        return service.getGenre(id);
    }

    @GetMapping("/mpa")
    public List<MPA> getAllMpa() {
        return service.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMPA(@PathVariable int id) {
        return service.getMPA(id);
    }
}
