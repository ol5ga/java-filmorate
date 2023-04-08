package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
@Qualifier
public class UserDbStorage implements UserStorage{
    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public User updateUser(User updateUser) {
        return null;
    }

    @Override
    public ArrayList<User> getAllUser() {
        return null;
    }

    @Override
    public User getUser(int id) {
        return null;
    }
}
