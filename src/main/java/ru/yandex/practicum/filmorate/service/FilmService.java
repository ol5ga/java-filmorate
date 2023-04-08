package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;


@Service
@RequiredArgsConstructor
public class FilmService {
       private final FilmStorage storage;

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
        Set<Integer> like = film.getLikes();
        like.add(userId);
        film.setLikes(like);
    }

    public void deleteLike(int id, int userId) {
        Film film = storage.getFilm(id);
        Set<Integer> like = film.getLikes();
        if (like.contains(userId)) {
            like.remove(userId);
        } else {
            throw new ChangeException("Такого пользователя не существует");
        }
        film.setLikes(like);
    }


    public List<Film> printTop(int count) {
        List<Film> films = new ArrayList<>(storage.getAllFilms());
        if (count > films.size()) {
            count = films.size();
        }
        return films.stream()
                .sorted((p0, p1) -> {
                    int comp = compare(p0.getLikes().size(), p1.getLikes().size());
                    return -1 * comp;
                }).limit(count)
                .collect(Collectors.toList());

    }

}
