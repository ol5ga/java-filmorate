package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PropertyDBStorageTest {

    private final PropertyDBStorage storage;
    private final FilmStorage filmStorage;
    private final UserDbStorage userStorage;
    MPA mpa = MPA.builder()
            .id(1)
            .name("G")
            .build();

    private void create() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100, mpa);
        filmStorage.createFilm(film);
        User user = new User("mail@mail.ru", "Nick Name", "User1", LocalDate.of(1946, 8, 20));
        userStorage.createUser(user);
    }

    @Test
    void testGetAllGenres() {
        List<Genre> genres = storage.getAllGenres();
        assertEquals(6, genres.size());
        assertEquals("Комедия", genres.get(0).getName());
        assertEquals(1, genres.get(0).getId());
    }

    @Test
    void testGetGenre() {
        Genre genre = storage.getGenre(2);
        assertEquals(2, genre.getId());
        assertEquals("Драма", genre.getName());

    }

    @Test
    void testGetAllMpa() {
        List<MPA> mpa = storage.getAllMpa();
        assertEquals(5, mpa.size());
        assertEquals("G", mpa.get(0).getName());
        assertEquals(1, mpa.get(0).getId());
    }

    @Test
    void testGetMpa() {
        MPA mpa = storage.getMpa(2);
        assertEquals(2, mpa.getId());
        assertEquals("PG", mpa.getName());
    }

    @Test
    void testAddLike() {
        create();
        storage.addLike(1, 1);
        Film filmExpect = filmStorage.getFilm(1);
        assertThat(filmExpect).hasFieldOrPropertyWithValue("rate", 1);
    }

    @Test
    void testDeleteLike() {
        create();
        User user2 = new User("friend@mail.ru", "friend", "Fri", LocalDate.of(1976, 8, 20));
        userStorage.createUser(user2);
        storage.addLike(1, 1);
        storage.addLike(1, 2);
        Film filmExpect = filmStorage.getFilm(1);
        assertThat(filmExpect).hasFieldOrPropertyWithValue("rate", 2);
        storage.deleteLike(1, 1);
        filmExpect = filmStorage.getFilm(1);
        assertThat(filmExpect).hasFieldOrPropertyWithValue("rate", 1);
    }

    @Test
    void testPrintTop() {
        create();
        User user2 = new User("friend@mail.ru", "friend", "Fri", LocalDate.of(1976, 8, 20));
        userStorage.createUser(user2);
        Film film2 = new Film("New film", "New film about friends", LocalDate.of(1999, 4, 30), 120, mpa);
        filmStorage.createFilm(film2);
        storage.addLike(1, 1);
        storage.addLike(1, 2);
        storage.addLike(2, 1);
        List<Film> top = storage.printTop(2);
        assertEquals(2, top.size());
        assertThat(top.get(0)).hasFieldOrPropertyWithValue("id", 1);
    }
}