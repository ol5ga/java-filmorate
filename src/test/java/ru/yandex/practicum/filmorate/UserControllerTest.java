package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    ObjectMapper mapper;


    @SneakyThrows
    @Test
    void testValidation() {
        User valid = new User("ol5ga@bk.ru", "ol5ga", "Ольга",LocalDate.of(1989,2,22));
        valid.setId(1);
        String validUser = mapper.writeValueAsString(valid);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(validUser))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.email", Matchers.containsString("ol5ga@bk.ru")))
                .andExpect(jsonPath("$.name").value("Ольга"));
    }

    @SneakyThrows
    @Test
    void testWrongEmail() {
        User valid = new User("ol5ga@bk.ru", "ol5ga", "Ольга",LocalDate.of(1989,2,22));
        valid.setId(1);
        String validUser = mapper.writeValueAsString(valid);
        User invalid = new User(" ", "ol5ga", "Ольга",LocalDate.of(1989,2,22));
        invalid.setId(2);
        String inValidUser = mapper.writeValueAsString(invalid);
        User invalid2 = new User("ol5ga", "ol5ga", "Ольга",LocalDate.of(1989,2,22));
        invalid.setId(3);
        String inValidUser2 = mapper.writeValueAsString(invalid);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(inValidUser))
                .andDo(print())
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(inValidUser2))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void testWrongLogin() {
        User valid = new User("ol5ga@bk.ru", "ol5ga", "Ольга",LocalDate.of(1989,2,22));
        valid.setId(1);
        String validUser = mapper.writeValueAsString(valid);
        User invalid = new User("ol5ga@bk.ru", " ", "Ольга",LocalDate.of(1989,2,22));
        invalid.setId(2);
        String inValidUser = mapper.writeValueAsString(invalid);
        User invalid2 = new User("ol5ga@bk.ru", "ol5  ga", "Ольга",LocalDate.of(1989,2,22));
        invalid.setId(3);
        String inValidUser2 = mapper.writeValueAsString(invalid);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(inValidUser))
                .andDo(print())
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(inValidUser2))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void testWrongName() {
        User valid = new User("ol5ga@bk.ru", "ol5ga", "Ольга",LocalDate.of(1989,2,22));
        valid.setId(1);
        String validUser = mapper.writeValueAsString(valid);
        User invalid = new User("ol5ga@bk.ru", "ol5ga", " ",LocalDate.of(1989,2,22));
        invalid.setId(2);
        String inValidUser = mapper.writeValueAsString(invalid);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(inValidUser))
                .andDo(print())
                .andExpect(jsonPath("$.name", Matchers.containsString(invalid.getLogin())));

    }

    @SneakyThrows
    @Test
    void testWrongBirthday() {
        User valid = new User("ol5ga@bk.ru", "ol5ga", "Ольга",LocalDate.of(1989,2,22));
        valid.setId(1);
        String validUser = mapper.writeValueAsString(valid);
        User invalid = new User("ol5ga@bk.ru", "ol5ga", "Ольга",LocalDate.of(2024,2,22));
        invalid.setId(2);
        String inValidUser = mapper.writeValueAsString(invalid);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(inValidUser))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
}