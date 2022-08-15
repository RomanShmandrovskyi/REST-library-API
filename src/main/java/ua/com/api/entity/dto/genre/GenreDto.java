package ua.com.api.entity.dto.genre;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.com.api.service.util.deserializer.CustomLongDeserializer;
import ua.com.api.service.util.deserializer.CustomStringDeserializer;

import javax.validation.constraints.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Schema(name = "Genre")
public class GenreDto {

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @NotNull(message = "Value 'genreId' is required!")
    @PositiveOrZero(message = "Value 'genreId' must be positive!")
    private Long genreId;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'genreName' is required!")
    @Size(max = 50, message = "Value 'genreName' cannot be longer than 50 characters!")
    private String genreName;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 1000, message = "Value 'description' cannot be longer than 1000 characters!")
    private String genreDescription = "";
}
