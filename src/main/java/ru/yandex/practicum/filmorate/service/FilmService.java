package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;


@Service
public class FilmService {


    private final InMemoryFilmStorage storage;

    @Autowired
    public FilmService(InMemoryFilmStorage storage){
        this.storage = storage;
    }

    public List<Film> getAllFilms() {

        return new ArrayList<Film>(storage.allFilms.values());
    }

    public void createFilm( Film film) {
        storage.createFilm(film);
    }
    public void updateFilm(Film updateFilm) {
        storage.updateFilm(updateFilm);
    }
    public void addLike(int id, int userId){
        Film film = storage.allFilms.get(id);
        film.likes.add(userId);
    }

    public void deleteLike(int id, int userId){
        Film film = storage.allFilms.get(id);
        film.likes.remove(userId);
    }


    public List<Film> printTop(int count) {
        List<Film> films = new ArrayList<>(storage.allFilms.values());
        return films.stream()
                .sorted((p0, p1) -> {
                    int comp = compare(p0.likes.size(), p1.likes.size());
                    return -1 * comp;
                }).limit(count)
                .collect(Collectors.toList());
     }
 


}
