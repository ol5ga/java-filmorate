package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{
    private int idGenerate = 0;
    private Map<Integer, User> allUsers = new HashMap<>();

    @Override
    public User createUser(User user) {
        validate(user);
        user = checkName(user);
        user.setId(++idGenerate);
        allUsers.put(user.getId(), user);
        return user;
    }
    @Override
    public void deleteUser(User user) {
        allUsers.remove(user.getId());

    }

    @Override
    public User updateUser(User updateUser) {
        if (allUsers.containsKey(updateUser.getId())) {
            allUsers.put(updateUser.getId(), updateUser);
        } else {
            throw new ChangeException("Такого пользователя не существует");
        }
        return updateUser;
    }

    private void validate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Неверный login");
        }

    }

    private User checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user = new User(user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
        }
        return user;
    }
}
