package ua.com.epam.entity.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.entity.dto.author.SimpleAuthorDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleGenreWithAuthorsDto {
    private Long genreId;
    private String genreName;
    List<SimpleAuthorDto> authors;
}