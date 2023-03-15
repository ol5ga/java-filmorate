package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;


@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

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
        Film film = storage.getFilm(id);
        film.likes.add(userId);
    }

    public void deleteLike(int id, int userId) {
        Film film = storage.getFilm(id);
        if (film.getLikes().contains(userId)) {
            film.likes.remove(userId);
        } else {
            throw new ChangeException("Такого пользователя не существует");
        }
    }


    public List<Film> printTop(int count) {
        List<Film> films = new ArrayList<>(storage.getAllFilms());
        if (count > films.size()) {
            count = films.size();
        }
        return films.stream()
                .sorted((p0, p1) -> {
                    int comp = compare(p0.likes.size(), p1.likes.size());
                    return -1 * comp;
                }).limit(count)
                .collect(Collectors.toList());

    }

}
