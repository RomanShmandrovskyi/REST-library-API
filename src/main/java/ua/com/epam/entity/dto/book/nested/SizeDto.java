package ua.com.epam.entity.dto.book.nested;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import ua.com.epam.service.util.deserializer.CustomDoubleDeserializer;

import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "Size")
public class SizeDto {

    @ApiModelProperty
    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    @PositiveOrZero(message = "Value 'height' must be positive!")
    private Double height = 0.0;

    @ApiModelProperty(position = 1)
    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    @PositiveOrZero(message = "Value 'width' must be positive!")
    private Double width = 0.0;

    @ApiModelProperty(position = 2)
    @JsonDeserialize(using = CustomDoubleDeserializer.class)
    @PositiveOrZero(message = "Value 'length' must be positive!")
    private Double length = 0.0;
}
