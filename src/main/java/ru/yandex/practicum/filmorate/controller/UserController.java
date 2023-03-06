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
        if (validate(user)) {
            if(checkName(user)) {
                user = new User(user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
            }
            user.setId(++idGenerate);
            allUsers.put(user.getId(),user);
            log.info("Добавление пользователя");

        } return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User updateUser){
        if (validate(updateUser)) {
            if(checkName(updateUser)) {
                updateUser = new User(updateUser.getEmail(), updateUser.getLogin(), updateUser.getLogin(), updateUser.getBirthday());
            }

                if (allUsers.containsKey(updateUser.getId())) {
                    allUsers.remove(updateUser.getId());
                    allUsers.put(updateUser.getId(),updateUser);
                } else{
                    throw new ChangeException("Такого пользователя не существует");
                }
                log.info("Изменение пользователя");


        } return updateUser;

    }

    private boolean validate(User user){
        if(user.getLogin().contains(" ")){
            throw new ValidationException("Неверный login");
        }return true;
    }

    private boolean checkName(User user){
        if(user.getName() == null || user.getName().isBlank()){
            return true;
        } else {
            return false;
        }
    }

}