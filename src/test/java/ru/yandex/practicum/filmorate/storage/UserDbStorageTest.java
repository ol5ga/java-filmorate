package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @BeforeEach
    void fullDB() {
        User user = new User("mail@mail.ru", "Nick Name", "User1", LocalDate.of(1946, 8, 20));
        userStorage.createUser(user);
    }

    @Test
    void testCreateUser() {
        User user = new User("mail@mail.ru", "Nick Name", "User", LocalDate.of(1946, 8, 20));
        userStorage.createUser(user);
        assertThat(user).hasFieldOrPropertyWithValue("id", 2);
        assertThat(user).hasFieldOrPropertyWithValue("name", "User");
    }

    @Test
    void testUpdateUser() {
        User update = new User("mail@mail.com", "Update", "Nick", LocalDate.of(1966, 8, 20));
        update.setId(1);
        User userExpect = userStorage.updateUser(update);
        assertThat(userExpect).hasFieldOrPropertyWithValue("id", 1);
        assertThat(userExpect).hasFieldOrPropertyWithValue("email", "mail@mail.com");
        assertThat(userExpect).hasFieldOrPropertyWithValue("login", "Update");
        assertThat(userExpect).hasFieldOrPropertyWithValue("name", "Nick");
    }

    @Test
    void testGetAllUser() {
        User user2 = new User("friend@mail.ru", "friend", "Fri", LocalDate.of(1976, 8, 20));
        userStorage.createUser(user2);
        List<User> allUsers = userStorage.getAllUser();
        assertEquals(2, allUsers.size());
        assertThat(allUsers.get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(allUsers.get(1)).hasFieldOrPropertyWithValue("id", 2);
    }

    @Test
    void testGetUser() {
        User user1 = userStorage.getUser(1);
        assertThat(user1).hasFieldOrPropertyWithValue("id", 1);
        assertThat(user1).hasFieldOrPropertyWithValue("name", "User1");
    }

    @Test
    void testAddFriend() {
        User user2 = new User("friend@mail.ru", "friend", "Fri", LocalDate.of(1976, 8, 20));
        userStorage.createUser(user2);
        userStorage.addFriend(2, 1);
        assertEquals(userStorage.printFriends(2).size(), 1);
        assertEquals(userStorage.printFriends(1).size(), 0);
        userStorage.addFriend(1, 2);
        assertEquals(userStorage.printFriends(1).size(), 1);
    }

    @Test
    void testDeleteFriend() {
        User user2 = new User("friend@mail.ru", "friend", "Fri", LocalDate.of(1976, 8, 20));
        userStorage.createUser(user2);
        userStorage.addFriend(2, 1);
        userStorage.addFriend(1, 2);
        assertEquals(userStorage.printFriends(1).size(), 1);
        assertEquals(userStorage.printFriends(2).size(), 1);
        userStorage.deleteFriend(1, 2);
        assertEquals(userStorage.printFriends(2).size(), 1);
        assertEquals(userStorage.printFriends(1).size(), 0);
    }

    @Test
    void testPrintFriends() {
        User user2 = new User("friend@mail.ru", "friend", "Fri", LocalDate.of(1976, 8, 20));
        userStorage.createUser(user2);
        userStorage.addFriend(2, 1);
        userStorage.addFriend(1, 2);
        List<User> friend = userStorage.printFriends(1);
        assertEquals(friend.size(), 1);
        assertEquals(friend.get(0), user2);
    }

    @Test
    void testPrintCommonFriends() {
        User user2 = new User("friend@mail.ru", "friend", "Fri", LocalDate.of(1976, 8, 20));
        userStorage.createUser(user2);
        User user3 = new User("friend@common.ru", "common", "", LocalDate.of(2000, 12, 20));
        userStorage.createUser(user3);
        userStorage.addFriend(2, 3);
        userStorage.addFriend(1, 3);
        List<User> friend = userStorage.printCommonFriends(1, 2);
        assertEquals(friend.size(), 1);
        assertEquals(friend.get(0), user3);
    }
}