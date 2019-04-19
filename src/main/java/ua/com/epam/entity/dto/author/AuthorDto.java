package ua.com.epam.entity.dto.author;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.entity.dto.author.nested.BirthDto;
import ua.com.epam.entity.dto.author.nested.NameDto;
import ua.com.epam.service.util.deserializer.CustomLongDeserializer;
import ua.com.epam.service.util.deserializer.CustomStringDeserializer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class AuthorDto {

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @NotNull(message = "Value 'authorId' is required!")
    @PositiveOrZero(message = "Value 'authorId' must be positive!")
    private Long authorId;

    @Valid
    @NotNull(message = "Value 'authorName' is required!")
    private NameDto authorName;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 30, message = "Value 'nationality' cannot be longer than 30 characters!")
    private String nationality = "";

    @Valid
    private BirthDto birth = new BirthDto();

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 1000, message = "Value 'description' cannot be longer than 1000 characters!")
    private String description = "";
}
