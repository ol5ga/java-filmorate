package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface PropertyStorage {

    public List<Genre> getAllGenres();


    public Genre getGenre(int id);


    public List<MPA> getAllMpa();


    public MPA getMpa(int id);

}
