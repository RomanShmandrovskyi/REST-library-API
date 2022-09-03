package ua.com.api.entity.dto.book.nested;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.com.api.service.util.deserializer.CustomIntegerDeserializer;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Schema(name = "Additional")
public class AdditionalDto {

    @JsonDeserialize(using = CustomIntegerDeserializer.class)
    @PositiveOrZero(message = "Value 'pagesCount' must be positive!")
    @Max(value = 10_000, message = "Value 'pagesCount' must be lower than 10,000")
    @Schema(defaultValue = "1")
    private Integer pagesCount;

    @Valid
    private SizeDto size = new SizeDto();
}
