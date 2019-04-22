package ua.com.epam.entity.dto.book.nested;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.service.util.deserializer.CustomDoubleDeserializer;

import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SizeDto {

    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    @PositiveOrZero(message = "Value 'height' must be positive!")
    private Double height = 0.0;

    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    @PositiveOrZero(message = "Value 'width' must be positive!")
    private Double width = 0.0;

    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    @PositiveOrZero(message = "Value 'length' must be positive!")
    private Double length = 0.0;
}
