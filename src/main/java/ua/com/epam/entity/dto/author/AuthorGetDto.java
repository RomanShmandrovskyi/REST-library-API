package ua.com.epam.entity.dto.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorGetDto {

    private long authorId;
    private NameDto authorName;
    private String nationality;
    private BirthDto birth;
    private String description;
    private long booksCount;
}
