package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ChangeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int idGenerate = 0;
    private Map<Integer, User> allUsers = new HashMap<>();

    @GetMapping()
    public List<User> getAllUsers() {

        return new ArrayList<User>(allUsers.values());
    }


    @PostMapping
    public User create(@RequestBody @Valid User user) {
        validate(user);
        user = checkName(user);
        user.setId(++idGenerate);
        allUsers.put(user.getId(), user);
        log.info("Добавление пользователя");
       return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User updateUser) {
        validate(updateUser);
        updateUser = checkName(updateUser);
        if (allUsers.containsKey(updateUser.getId())) {
            allUsers.put(updateUser.getId(), updateUser);
        } else {
            throw new ChangeException("Такого пользователя не существует");
        }
        log.info("Изменение пользователя");
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