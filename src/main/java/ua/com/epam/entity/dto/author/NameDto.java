package ua.com.epam.entity.dto.author;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.service.util.deserializer.CustomStringDeserializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NameDto {

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'first' is required!")
    @Size(min = 1, max = 50, message = "Value 'first' cannot be longer than 50 characters!")
    private String first;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'second' is required!")
    @Size(min = 1, max = 50, message = "Value 'second' cannot be longer than 50 characters!")
    private String second;
}
