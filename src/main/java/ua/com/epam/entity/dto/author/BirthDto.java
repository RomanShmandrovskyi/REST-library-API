package ua.com.epam.entity.dto.author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class BirthDto {

    @PastOrPresent(message = "Value 'birth.date' must be past or present!")
    private Date date;

    @Max(value = 50, message = "Value 'birth.country' is too long!")
    private String country;

    @Max(value = 50, message = "Value 'birth.city' is too long!")
    private String city;
}
