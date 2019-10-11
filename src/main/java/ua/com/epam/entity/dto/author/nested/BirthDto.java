package ua.com.epam.entity.dto.author.nested;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import ua.com.epam.service.util.deserializer.CustomDateDeserializer;
import ua.com.epam.service.util.deserializer.CustomStringDeserializer;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "Birth")
public class BirthDto {

    @ApiModelProperty(example = "1973-03-28")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @PastOrPresent(message = "Value 'date' must be past or present!")
    private LocalDate date;

    @ApiModelProperty(position = 1)
    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 100, message = "Value 'country' cannot be longer than 100 characters!")
    private String country = "";

    @ApiModelProperty(position = 2)
    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 100, message = "Value 'city' cannot be longer than 100 characters!")
    private String city = "";
}
