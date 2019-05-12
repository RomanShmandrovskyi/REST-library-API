package ua.com.epam.entity.dto.author;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.entity.dto.author.nested.NameDto;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("SimpleAuthor")
public class SimpleAuthorDto {
    private Long id;
    private NameDto name;
}
