package ua.com.epam.entity.dto.book.nested;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.service.util.deserializer.CustomIntegerDeserializer;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Additional")
public class AdditionalDto {

    @JsonDeserialize(using = CustomIntegerDeserializer.class)
    @PositiveOrZero(message = "Value 'pageCount' must be positive!")
    @Max(value = 10_000, message = "Value 'pageCount' must be lower than 10,000")
    private Integer pageCount = 0;

    @Valid
    private SizeDto size = new SizeDto();
}
