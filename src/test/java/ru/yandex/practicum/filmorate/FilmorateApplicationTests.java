package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;



import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FilmController filmController;

	@Autowired
	private UserController userController;

	@Autowired
	ObjectMapper mapper;

	@Test
	public void contextLoads() throws Exception {
		assertThat(filmController).isNotNull();
		assertThat(userController).isNotNull();
	}


}
