package ua.com.epam.entity.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorGenreDto {
    private Long genreId;
    private String genreName;
}
