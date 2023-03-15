package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

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
        user1.friends.add(friendId);
        user2.friends.add(id);
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
        List<Integer> friendsId = new ArrayList<>(user.friends.stream().collect(Collectors.toList()));
        return friendsId.stream()
                .map(s -> {
                    User friend = storage.getUser(s);
                    return friend;
                })
                .collect(Collectors.toList());
    }

    public List<User> printCommonFriends(int id, int otherId) {
        List<Integer> commonFriends = new ArrayList<>();
        User user1 = storage.getUser(id);
        User user2 = storage.getUser(otherId);
        Set<Integer> friends1 = user1.getFriends();
        Set<Integer> friends2 = user2.getFriends();
        for (Integer userId : friends1) {
            if (friends2.contains(userId)) {
                commonFriends.add(userId);
            }
        }
        return commonFriends.stream()
                .map(s -> {
                    User friend = storage.getUser(s);
                    return friend;
                })
                .collect(Collectors.toList());
    }
}
