package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ParameterDBStorageTest {

    private final ParameterDBStorage storage;
    private final FilmStorage filmStorage;
    private final UserDbStorage userStorage;
    MPA mpa = MPA.builder()
            .id(1)
            .name("G")
            .build();

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


}