package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface PropertyStorage {

    List<Genre> getAllGenres();


    Genre getGenre(int id);


    List<MPA> getAllMpa();


    MPA getMpa(int id);

}
