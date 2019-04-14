package ua.com.epam.entity.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenrePostDto {
    @NotNull(message = "Value 'genreId' is required!")
    @PositiveOrZero(message = "Value 'genreId' must be positive!")
    private long genreId;

    @NotBlank(message = "Value 'genreName' is required!")
    @Max(value = 50, message = "Value 'genreName' is too long!")
    private String genreName;

    @Max(value = 1000, message = "Value 'description' is too long!")
    private String description;
}
