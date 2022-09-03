package ua.com.api.entity.dto.genre;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.com.api.service.util.deserializer.CustomStringDeserializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Schema(name = "GenreToCreate")
public class GenreWithoutIdDto {

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'name' is required!")
    @Size(max = 50, message = "Value 'name' cannot be longer than 50 characters!")
    private String name;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 1000, message = "Value 'description' cannot be longer than 1000 characters!")
    private String description = "";
}
