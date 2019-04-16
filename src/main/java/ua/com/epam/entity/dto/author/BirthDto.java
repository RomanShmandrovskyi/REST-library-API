package ua.com.epam.entity.dto.author;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.service.util.deserializer.CustomDateDeserializer;
import ua.com.epam.service.util.deserializer.CustomStringDeserializer;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BirthDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @PastOrPresent(message = "Value 'date' must be past or present!")
    private Date date;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 50, message = "Value 'country' cannot be longer than 50 characters!")
    private String country = "";

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Size(max = 50, message = "Value 'city' cannot be longer than 50 characters!")
    private String city = "";
}
