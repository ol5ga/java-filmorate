package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private int idGenerate = 0;
    private final UserService userService;

    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        validate(user);
        user = checkName(user);
        user.setId(++idGenerate);
        userService.addUser(user);
        log.info("Добавление пользователя");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User updateUser) {
        validate(updateUser);
        updateUser = checkName(updateUser);
        userService.updateUser(updateUser);
        log.info("Изменение пользователя");
        return updateUser;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> printFriends(@PathVariable int id) {
        return userService.printFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> printCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.printCommonFriends(id, otherId);
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