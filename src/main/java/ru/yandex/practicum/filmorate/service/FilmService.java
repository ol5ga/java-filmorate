package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ParameterDBStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;
    private final ParameterDBStorage property;

    private final LikesStorage likesStorage;

    public List<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    public void createFilm(Film film) {
        storage.createFilm(film);
    }

    public void updateFilm(Film updateFilm) {
        storage.updateFilm(updateFilm);
    }

    public Film getUser(int id) {
        return storage.getFilm(id);
    }

    public void addLike(int id, int userId) {
        likesStorage.addLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        likesStorage.deleteLike(id, userId);
    }


    public List<Film> printTop(int count) {
        return likesStorage.printTop(count);
    }

}
