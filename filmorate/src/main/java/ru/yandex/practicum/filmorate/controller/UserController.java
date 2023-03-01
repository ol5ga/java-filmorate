package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    int idGenerate = 0;
    private List<Users> allUsers = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping()
    public List<Users> getAllUsers() {
        return allUsers;
    }

    @GetMapping("/user")
    public Users getUser() {
        return new Users("ol5ga","ol5ga","Ольга", LocalDate.of(1989,02,22));
    }

    @PostMapping(value = "/user")
    public Users create(@RequestBody Users user) {
        user.setId(++idGenerate);
        allUsers.add(user);
        log.info("Добавление пользователя");
        return user;

    }

    @PutMapping
    public Users updateUser(@RequestBody Users updateUser){
        for (Users user : allUsers) {
            if (user.getId() == updateUser.getId()){
                allUsers.remove(user);
                allUsers.add(updateUser);
            }
            log.info("Изменение пользователя");
        }
        return updateUser;
    }

}
