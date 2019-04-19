package ua.com.epam.entity.dto.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.entity.dto.author.nested.NameDto;
import ua.com.epam.entity.dto.genre.SimpleGenreDto;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleAuthorWithGenresDto {
    private Long authorId;
    private NameDto authorName;
    private List<SimpleGenreDto> genres;
}
