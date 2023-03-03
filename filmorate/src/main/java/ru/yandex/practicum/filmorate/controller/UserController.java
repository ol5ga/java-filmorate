package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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


    @PostMapping
    public Users create(@RequestBody Users user) {
        if (validate(user)) {
            if(user.getName() == null || user.getName().isBlank()) {
                user = new Users(user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
            }
            user.setId(++idGenerate);
            allUsers.add(user);
            log.info("Добавление пользователя");

        } return user;
     }

    @PutMapping
    public Users updateUser(@RequestBody Users updateUser){
        if (validate(updateUser)) {
            if(updateUser.getName() == null || updateUser.getName().isBlank()) {
                updateUser = new Users(updateUser.getEmail(), updateUser.getLogin(), updateUser.getLogin(), updateUser.getBirthday());
            }
            for (Users user : allUsers) {
                if (user.getId() == updateUser.getId()) {
                    allUsers.remove(user);
                    allUsers.add(updateUser);
                } else{
                    throw new ValidationException("Такого пользователя не существует");
                }
                log.info("Изменение пользователя");

            }
        } return updateUser;

    }

    private boolean validate(Users user){
        if (user.getEmail() == null || user.getEmail().isBlank()){
            throw new ValidationException("Неверный e-mail");
        } else if(!user.getEmail().contains("@")){
            throw new ValidationException("Неверный e-mail");
        } else if(user.getLogin().isBlank() || user.getLogin().contains(" ")){
            throw new ValidationException("Неверный login");
        } else if(user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else {
            return true;
        }
    }

}
