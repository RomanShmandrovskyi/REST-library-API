package ua.com.epam.entity.dto.genre;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import ua.com.epam.service.util.deserializer.CustomLongDeserializer;
import ua.com.epam.service.util.deserializer.CustomStringDeserializer;

import javax.validation.constraints.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "Genre")
public class GenreDto {

    @ApiModelProperty(required = true)
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @NotNull(message = "Value 'genreId' is required!")
    @PositiveOrZero(message = "Value 'genreId' must be positive!")
    private Long genreId;

    @ApiModelProperty(required = true, position = 1)
    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'genreName' is required!")
    @Size(max = 50, message = "Value 'genreName' cannot be longer than 50 characters!")
    private String genreName;

    @ApiModelProperty(position = 2)
    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 1000, message = "Value 'description' cannot be longer than 1000 characters!")
    private String genreDescription = "";
}
