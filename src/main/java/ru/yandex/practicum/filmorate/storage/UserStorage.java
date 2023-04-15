package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {

    User createUser(User user);

     User updateUser(User updateUser);

    List<User> getAllUser();

    User getUser(int id);

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<User> printFriends(int id);

    List<User> printCommonFriends(int id, int otherId);
}
