package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface ParameterStorage {

    List<Genre> getAllGenres();


    Genre getGenre(int id);


    List<MPA> getAllMpa();


    MPA getMpa(int id);

}
