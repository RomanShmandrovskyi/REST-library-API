package ua.com.epam.service.mapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorPostDto;

public class AuthorPostDtoToAuthor implements Converter<AuthorPostDto, Author> {

    @Override
    public Author convert(MappingContext<AuthorPostDto, Author> mappingContext) {
        AuthorPostDto source = mappingContext.getSource();

        Author author = new Author();
        author.setAuthorId(source.getAuthorId());
        author.setFirstName(source.getAuthorName().getFirst());
        author.setSecondName(source.getAuthorName().getSecond());
        author.setNationality(source.getNationality());
        author.setDescription(source.getDescription());
        author.setBirthDate(source.getBirth().getDate());
        author.setBirthCity(source.getBirth().getCity());
        author.setBirthCountry(source.getBirth().getCountry());

        return author;
    }
}
