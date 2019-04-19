package ua.com.epam.entity.dto.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.entity.dto.author.nested.NameDto;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleAuthorDto {
    private Long id;
    private NameDto name;
}
