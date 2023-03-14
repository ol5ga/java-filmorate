package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
   private final InMemoryUserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage){
        this.storage = storage;
    }
    public List<User> getAllUsers() {

        return new ArrayList<User>(storage.allUsers.values());
    }

    public void addUser(@RequestBody @Valid User user) {
        storage.createUser(user);
    }
    public void updateUser(@RequestBody @Valid User updateUser) {
        storage.updateUser(updateUser);
    }
    public void addFriend(int id, int friendId){
        User user1 = storage.allUsers.get(id);
        User user2 = storage.allUsers.get(friendId);
        user1.friends.add(user2);
        user2.friends.add(user1);
    }

    public void deleteFriend(int id, int friendId){
        User user1 = storage.allUsers.get(id);
        User user2 = storage.allUsers.get(friendId);
        user1.friends.remove(user2);
        user2.friends.remove(user1);
    }

    public List<User> printFriends(int id){
        User user = storage.allUsers.get(id);
        return new ArrayList<>(user.friends.stream().collect(Collectors.toList()));
    }

    public List<User> printCommonFriends(int id, int otherId){
        List<User> commonFriends = new ArrayList<>();
        User user1 = storage.allUsers.get(id);
        User user2 = storage.allUsers.get(otherId);
        Set<User> friends1 = user1.getFriends();
        Set<User> friends2 = user2.getFriends();
        for (User user : friends1) {
            if(friends2.contains(user)){
                commonFriends.add(user);
            }
        }
        return commonFriends;
    }
}
