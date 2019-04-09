package ua.com.epam.entity.dto.genre;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GenreGetDto {

    private long genreId;
    private String genreName;
    private String description;
    private int booksCount;
}
