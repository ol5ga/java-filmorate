package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    public Map<Integer, User> allUsers = new HashMap<>();


    @Override
    public User createUser(User user) {
        allUsers.put(user.getId(), user);
        return user;
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

    @Override
    public ArrayList<User> getAllUser() {
        return new ArrayList<User>(allUsers.values());
    }

    @Override
    public User getUser(int id) {
        if (!allUsers.containsKey(id)) {
            throw new ChangeException("Такого пользователя не существует");
        }
        return allUsers.get(id);
    }

    @Override
    public void addFriend(int id, int friendId) {
        User user1 = getUser(id);
        User user2 = getUser(friendId);
        if (user2.getApplications().contains(id)) {
            user1.friends.add(friendId);
            user2.friends.add(id);
            user2.applications.remove(id);
        } else {
            user1.applications.add(friendId);
        }
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        User user1 = getUser(id);
        User user2 = getUser(friendId);
        if (user1.friends.contains(friendId) && user2.friends.contains(id)) {
            user1.friends.remove(id);
            user2.friends.remove(friendId);
        } else {
            throw new ChangeException("Неверные пользователи");
        }
    }

    @Override
    public List<User> printFriends(int id) {
        User user = getUser(id);
        return user.getFriends().stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> printCommonFriends(int id, int otherId) {
        User user1 = getUser(id);
        User user2 = getUser(otherId);
        return user1.getFriends().stream()
                .filter(user2.getFriends()::contains)
                .map(id1 -> getUser(id1))
                .collect(Collectors.toList());
    }
}
