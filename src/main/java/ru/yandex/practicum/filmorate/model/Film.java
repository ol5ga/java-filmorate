package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import ru.yandex.practicum.filmorate.validate.Release;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;


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
    private Set<Integer> likes = new HashSet<>();
    private final List<String> genre = new ArrayList<>();
    private MPA age;


    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}

