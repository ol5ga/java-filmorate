package ru.yandex.practicum.filmorate.validate;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseConstraintValidator implements ConstraintValidator<Release, LocalDate> {
    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate data, ConstraintValidatorContext constraintValidatorContext) {
        if(data.isBefore(RELEASE_DATE)){
            throw new ValidationException("Дата релиза не верна");
        } return true;
    }
}
