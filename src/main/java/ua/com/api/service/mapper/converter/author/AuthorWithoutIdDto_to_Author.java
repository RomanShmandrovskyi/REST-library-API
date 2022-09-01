package ua.com.api.service.mapper.converter.author;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.api.entity.Author;
import ua.com.api.entity.dto.author.AuthorWithoutIdDto;

public class AuthorWithoutIdDto_to_Author implements Converter<AuthorWithoutIdDto, Author> {

    @Override
    public Author convert(MappingContext<AuthorWithoutIdDto, Author> mappingContext) {
        AuthorWithoutIdDto source = mappingContext.getSource();

        Author author = new Author();
        author.setFirstName(source.getName().getFirst());
        author.setLastName(source.getName().getLast());
        author.setFullName(source.getName().getFirst() + " " + source.getName().getLast());
        author.setNationality(source.getNationality());
        author.setDescription(source.getDescription());
        author.setBirthDate(source.getBirth().getDate());
        author.setBirthCity(source.getBirth().getCity());
        author.setBirthCountry(source.getBirth().getCountry());

        return author;
    }
}
