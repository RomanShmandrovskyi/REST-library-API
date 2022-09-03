package ua.com.api.entity.dto.book.nested;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.com.api.service.util.deserializer.CustomDoubleDeserializer;

import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Schema(name = "Size")
public class SizeDto {

    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    @PositiveOrZero(message = "Value 'height' must be positive!")
    private Double height;

    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    @PositiveOrZero(message = "Value 'width' must be positive!")
    private Double width;

    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    @PositiveOrZero(message = "Value 'length' must be positive!")
    private Double length;
}
