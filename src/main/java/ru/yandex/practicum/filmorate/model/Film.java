package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.Release;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
public class Film {

    private int id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    @NotNull
    private final String description;
    @NotNull
    @Release
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
}

