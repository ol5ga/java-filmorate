package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public List<User> getAllUsers() {
        return storage.getAllUser();
    }

    public void addUser(User user) {
        storage.createUser(user);
    }

    public void updateUser(User updateUser) {
        storage.updateUser(updateUser);
    }

    public User getUser(int id) {
        return storage.getUser(id);
    }

    public void addFriend(int id, int friendId) {
        storage.addFriend(id,friendId);
    }

    public void deleteFriend(int id, int friendId) {
        storage.deleteFriend(id,friendId);
    }

    public List<User> printFriends(int id) {
      return storage.printFriends(id);
    }

    public List<User> printCommonFriends(int id, int otherId) {
       return storage.printCommonFriends(id,otherId);
    }
}
