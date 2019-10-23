package ua.com.epam.entity.dto.author;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
@ApiModel(value = "Author")
public class AuthorDto {

    @ApiModelProperty(required = true)
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @NotNull(message = "Value 'authorId' is required!")
    @PositiveOrZero(message = "Value 'authorId' must be positive!")
    private Long authorId;

    @ApiModelProperty(required = true, position = 1)
    @Valid @NotNull(message = "Object 'authorName' is required!")
    private NameDto authorName;

    @ApiModelProperty(position = 2)
    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 30, message = "Value 'nationality' cannot be longer than 30 characters!")
    private String nationality = "";

    @ApiModelProperty(position = 3)
    @Valid
    private BirthDto birth = new BirthDto();

    @ApiModelProperty(position = 4)
    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 1000, message = "Value 'authorDescription' cannot be longer than 1000 characters!")
    private String authorDescription = "";
}
