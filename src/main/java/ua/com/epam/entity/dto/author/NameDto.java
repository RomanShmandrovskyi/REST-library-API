package ua.com.epam.entity.dto.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NameDto {

    @NotBlank(message = "Value 'name.first' is required!")
    @Size(min = 1, max = 50, message = "Value 'name.first' is too long!")
    private String first;

    @NotBlank(message = "Value 'name.second' is required!")
    @Size(min = 1, max = 50, message = "Value 'name.second' is too long!")
    private String second;
}
