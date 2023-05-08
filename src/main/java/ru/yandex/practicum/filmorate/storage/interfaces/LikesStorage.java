package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {
    void addLike(int id, int userId);

    void deleteLike(int id, int userId);

    List<Film> printTop(int count);
}
