package ua.com.api.entity.dto.author.nested;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.com.api.service.util.deserializer.CustomDateDeserializer;
import ua.com.api.service.util.deserializer.CustomStringDeserializer;
import ua.com.api.service.util.serializer.CustomDateSerializer;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Schema(name = "Birth")
public class BirthDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @PastOrPresent(message = "Value 'date' must be past or present!")
    private LocalDate date;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 100, message = "Value 'country' cannot be longer than 100 characters!")
    private String country = "";

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 100, message = "Value 'city' cannot be longer than 100 characters!")
    private String city = "";
}
