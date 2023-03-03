package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmController filmController;
    @Autowired
    ObjectMapper mapper;


    @SneakyThrows
    @Test
    void testValidation() {
        Film valid = new Film("FilmName", "FilmDescription", LocalDate.of(2020, 2, 15), 75);
        valid.setId(1);
        String validFilm = mapper.writeValueAsString(valid);
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(validFilm))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name", Matchers.containsString("FilmName")))
                .andExpect(jsonPath("$.description").value("FilmDescription"));
    }

    @SneakyThrows
    @Test
    void testWrongName() {
        Film valid = new Film("FilmName", "FilmDescription", LocalDate.of(2020, 2, 15), 75);
        valid.setId(1);
        String validFilm = mapper.writeValueAsString(valid);
        Film invalid = new Film(" ", "FilmDescription2", LocalDate.of(2021, 2, 15), 75);
        invalid.setId(2);
        String inValidFilm = mapper.writeValueAsString(invalid);
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(inValidFilm))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testLongDescription() {
        Film valid = new Film("FilmName", "FilmDescription", LocalDate.of(2020, 2, 15), 75);
        valid.setId(1);
        String validFilm = mapper.writeValueAsString(valid);
        Film invalid = new Film("FilmName2", "FilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescription", LocalDate.of(2021, 2, 15), 75);
        invalid.setId(2);
        String inValidFilm = mapper.writeValueAsString(invalid);
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(inValidFilm))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testWrongRelease() {
        Film valid = new Film("FilmName", "FilmDescription", LocalDate.of(2020, 2, 15), 75);
        valid.setId(1);
        String validFilm = mapper.writeValueAsString(valid);
        Film invalid = new Film("FilmName2", "FilmDescription", LocalDate.of(1894, 2, 15), 75);
        invalid.setId(2);
        String inValidFilm = mapper.writeValueAsString(invalid);
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(inValidFilm))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testWrongDuration() {
        Film valid = new Film("FilmName", "FilmDescription", LocalDate.of(2020, 2, 15), 75);
        valid.setId(1);
        String validFilm = mapper.writeValueAsString(valid);
        Film invalid = new Film("FilmName2", "FilmDescription", LocalDate.of(2020, 2, 15), -75);
        invalid.setId(2);
        String inValidFilm = mapper.writeValueAsString(invalid);
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(inValidFilm))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
