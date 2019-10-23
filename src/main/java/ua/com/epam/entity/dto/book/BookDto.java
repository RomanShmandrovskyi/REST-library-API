package ua.com.epam.entity.dto.book;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import ua.com.epam.entity.dto.book.nested.AdditionalDto;
import ua.com.epam.service.util.deserializer.CustomIntegerDeserializer;
import ua.com.epam.service.util.deserializer.CustomLongDeserializer;
import ua.com.epam.service.util.deserializer.CustomStringDeserializer;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@ApiModel(value = "Book")
public class BookDto {

    @ApiModelProperty(required = true)
    @JsonDeserialize(using = CustomLongDeserializer.class)
    @NotNull(message = "Value 'bookId' is required!")
    @PositiveOrZero(message = "Value 'bookId' must be positive!")
    private Long bookId;

    @ApiModelProperty(required = true, position = 1)
    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'bookName' is required!")
    @Size(min = 1, max = 255, message = "Value 'bookName' cannot be longer than 255 characters!")
    private String bookName;

    @ApiModelProperty(required = true, position = 2)
    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'bookLanguage' is required!")
    @Size(min = 1, max = 50, message = "Value 'bookLanguage' cannot be longer than 50 characters!")
    private String bookLanguage;

    @ApiModelProperty(position = 3)
    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 1000, message = "Value 'description' cannot be longer than 1000 characters!")
    private String bookDescription = "";

    @ApiModelProperty(position = 4)
    @Valid
    private AdditionalDto additional;

    @ApiModelProperty(position = 5)
    @JsonDeserialize(using = CustomIntegerDeserializer.class)
    @Max(value = 2019, message = "Publication year must be lower then 2019")
    private Integer publicationYear = 0;
}
