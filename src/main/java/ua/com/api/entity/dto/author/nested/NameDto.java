package ua.com.api.entity.dto.author.nested;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.com.api.service.util.deserializer.CustomStringDeserializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Schema(name = "Name")
public class NameDto {

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'first' is required and can't be blank!")
    @Size(max = 50, message = "Value 'first' cannot be longer than 50 characters!")
    private String first;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    @NotBlank(message = "Value 'last' is required and can't be blank!!")
    @Size(max = 50, message = "Value 'last' cannot be longer than 50 characters!")
    private String last;
}
