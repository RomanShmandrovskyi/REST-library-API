package ua.com.epam.entity.dto.author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.epam.entity.Author;

@Setter
@Getter
@NoArgsConstructor
public class AuthorGetDto {

    private long authorId;
    private NameDto authorName;
    private String nationality;
    private BirthDto birth;
    private String description;
    private int booksCount;

    public AuthorGetDto(Author author) {
        NameDto name = new NameDto();
        name.setFirst(author.getFirstName());
        name.setSecond(author.getSecondName());

        BirthDto birth = new BirthDto();
        birth.setDate(author.getBirthDate());
        birth.setCity(author.getBirthCity());
        birth.setCountry(author.getBirthCountry());

        this.authorId = author.getAuthorId();
        this.nationality = author.getNationality();
        this.description = author.getDescription();
        this.authorName = name;
        this.birth = birth;
    }
}
