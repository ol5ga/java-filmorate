package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;

public interface UserStorage {

    User createUser(User user);

    void deleteUser(User user);

    User updateUser(User updateUser);

    ArrayList<User> getAllUser();

    User getUser(int id);
}
