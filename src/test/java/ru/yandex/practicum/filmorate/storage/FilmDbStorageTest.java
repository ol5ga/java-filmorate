package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;
    MPA mpa = MPA.builder()
            .id(1)
            .name("G")
            .build();

    @BeforeEach
    void fullDB() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100, mpa);
        filmStorage.createFilm(film);
    }

    @Test
    void TestCreateFilm() {
        Film film1 = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100, mpa);
        filmStorage.createFilm(film1);
        assertThat(film1).hasFieldOrPropertyWithValue("id", 2);
        assertThat(film1).hasFieldOrPropertyWithValue("name", "nisi eiusmod");
    }

    @Test
    void TestUpdateFilm() {
        Genre genre = Genre.builder()
                .id(2)
                .name("Драма")
                .build();
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        Film update = new Film("update", "newDescription", LocalDate.of(1977, 3, 25), 100, mpa, genres);
        update.setId(1);
        Film expectFilm = filmStorage.updateFilm(update);
        assertThat(expectFilm).hasFieldOrPropertyWithValue("id", 1);
        assertThat(expectFilm).hasFieldOrPropertyWithValue("name", "update");
        assertThat(expectFilm).hasFieldOrPropertyWithValue("description", "newDescription");
        assertEquals(1, expectFilm.getGenres().size());
    }


    @Test
    void TestGetAllFilms() {
        Film film2 = new Film("New film", "New film about friends", LocalDate.of(1999, 4, 30), 120, mpa);
        filmStorage.createFilm(film2);
        List<Film> allFilms = filmStorage.getAllFilms();
        assertEquals(2, allFilms.size());
        assertThat(allFilms.get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(allFilms.get(1)).hasFieldOrPropertyWithValue("id", 2);

    }

    @Test
    void TestGetFilm() {
        Film filmExpect = filmStorage.getFilm(1);
        assertThat(filmExpect).hasFieldOrPropertyWithValue("id", 1);
        assertThat(filmExpect).hasFieldOrPropertyWithValue("name", "nisi eiusmod");
    }
}