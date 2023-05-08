package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LikesDBStorageTest {
    private final FilmStorage filmStorage;
    private final UserDbStorage userStorage;
    private final LikesStorage likesStorage;
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
    void testAddLike() {
        create();
        likesStorage.addLike(1, 1);
        Film filmExpect = filmStorage.getFilm(1);
        assertThat(filmExpect).hasFieldOrPropertyWithValue("rate", 1);
    }

    @Test
    void testDeleteLike() {
        create();
        User user2 = new User("friend@mail.ru", "friend", "Fri", LocalDate.of(1976, 8, 20));
        userStorage.createUser(user2);
        likesStorage.addLike(1, 1);
        likesStorage.addLike(1, 2);
        Film filmExpect = filmStorage.getFilm(1);
        assertThat(filmExpect).hasFieldOrPropertyWithValue("rate", 2);
        likesStorage.deleteLike(1, 1);
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
        likesStorage.addLike(1, 1);
        likesStorage.addLike(1, 2);
        likesStorage.addLike(2, 1);
        List<Film> top = likesStorage.printTop(2);
        assertEquals(2, top.size());
        assertThat(top.get(0)).hasFieldOrPropertyWithValue("id", 1);
    }
}
