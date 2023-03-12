package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int idGenerate = 0;
    private final UserService userService;
    private final InMemoryUserStorage storage;

    @Autowired
    public UserController(UserService userService,InMemoryUserStorage storage){
        this.userService= userService;
        this.storage = storage;
    }


    @GetMapping()
    public List<User> getAllUsers() {

        return new ArrayList<User>(storage.allUsers.values());
    }


    @PostMapping
    public User create(@RequestBody @Valid User user) {

        validate(user);
        user = checkName(user);
        user.setId(++idGenerate);
        storage.createUser(user);
        log.info("Добавление пользователя");
       return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User updateUser) {
        validate(updateUser);
        updateUser = checkName(updateUser);
        storage.updateUser(updateUser);
        log.info("Изменение пользователя");
        return updateUser;

    }

    @PutMapping
    public void addFriend(){
//    }users/{id}/friends/{friendId}
    }

    @DeleteMapping
    public void deleteFriend() {
//        /users/{id}/friends/{friendId}
    }

    @GetMapping
    public List<Long> printFriends() {
//            }users/{id}/friends
        return new ArrayList<>();
    }

    @GetMapping
    public List<Long> printCommonfriends() {
//        users / {id} / friends / common / {otherId}
        return new ArrayList<>();
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