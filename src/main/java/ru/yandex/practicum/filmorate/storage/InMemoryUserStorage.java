package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{
   public Map<Integer, User> allUsers = new HashMap<>();

    @Override
    public User createUser(User user) {
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


}
