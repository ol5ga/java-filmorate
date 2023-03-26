package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
        User user1 = storage.getUser(id);
        User user2 = storage.getUser(friendId);
        if (user2.getApplications().contains(id)) {
            user1.friends.add(friendId);
            user2.friends.add(id);
            user2.applications.remove(id);
        } else {
            user1.applications.add(friendId);
        }
    }

    public void deleteFriend(int id, int friendId) {
        User user1 = storage.getUser(id);
        User user2 = storage.getUser(friendId);
        if(user1.friends.contains(friendId) && user2.friends.contains(id)) {
            user1.friends.remove(id);
            user2.friends.remove(friendId);
        } else {
            throw new ChangeException("Неверные пользователи");
        }
    }

    public List<User> printFriends(int id) {
        User user = storage.getUser(id);
        return user.getFriends().stream()
                .map(storage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> printCommonFriends(int id, int otherId) {
        User user1 = storage.getUser(id);
        User user2 = storage.getUser(otherId);
        return user1.getFriends().stream()
                .filter(user2.getFriends()::contains)
                .map(storage::getUser)
                .collect(Collectors.toList());
    }
}
