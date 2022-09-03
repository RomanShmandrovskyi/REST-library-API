package ua.com.api.entity.dto.book;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.com.api.entity.dto.book.nested.AdditionalDto;
import ua.com.api.service.util.deserializer.CustomStringDeserializer;
import ua.com.api.service.util.deserializer.CustomYearDeserializer;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Schema(name = "BookToCreate")
public class BookWithoutIdDto {

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'bookName' is required!")
    @Size(min = 1, max = 255, message = "Value 'bookName' cannot be longer than 255 characters!")
    private String bookName;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 1000, message = "Value 'description' cannot be longer than 1000 characters!")
    private String bookDescription;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'bookLanguage' is required!")
    @Size(min = 1, max = 50, message = "Value 'bookLanguage' cannot be longer than 50 characters!")
    private String bookLanguage;

    @Valid
    private AdditionalDto additional;

    @JsonDeserialize(using = CustomYearDeserializer.class)
    @Positive(message = "Publication year must be positive")
    @Schema(defaultValue = "1970")
    private Integer publicationYear;
}
