package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.PropertyStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyStorage storage;


    public List<Genre> getAllGenres() {
        return storage.getAllGenres();
    }

    public Genre getGenre(int id){
        return storage.getGenre(id);
    }

    public List<MPA> getAllMpa(){
        return storage.getAllMpa();
    }

    public MPA getMPA(int id) {
        return storage.getMpa(id);
    }
}
