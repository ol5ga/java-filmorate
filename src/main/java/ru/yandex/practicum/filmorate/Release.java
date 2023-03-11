package ru.yandex.practicum.filmorate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Constraint(validatedBy = ReleaseConstraintValidator.class)
    public @interface Release{
        String message() default "{Release}";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

