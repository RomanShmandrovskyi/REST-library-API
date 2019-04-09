package ua.com.epam.entity.dto.author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
public class AuthorPostDto {

    @NotNull(message = "Value 'authorId' is required!")
    @PositiveOrZero(message = "Value 'authorId' must be positive!")
    private long authorId;

    @NotNull(message = "Value 'name' can not be null!")
    private NameDto authorName;

    @Max(value = 30, message = "Value 'nationality' is too long!")
    private String nationality;

    private BirthDto birth;

    @Max(value = 1000, message = "Value 'description' is too long!")
    private String description;
}
