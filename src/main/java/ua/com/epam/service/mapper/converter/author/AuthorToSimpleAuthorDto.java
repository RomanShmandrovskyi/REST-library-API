package ua.com.epam.service.mapper.converter.author;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.nested.NameDto;
import ua.com.epam.entity.dto.author.SimpleAuthorDto;

public class AuthorToSimpleAuthorDto implements Converter<Author, SimpleAuthorDto> {
    @Override
    public SimpleAuthorDto convert(MappingContext<Author, SimpleAuthorDto> mappingContext) {
        Author a = mappingContext.getSource();

        SimpleAuthorDto sad = new SimpleAuthorDto();
        sad.setId(a.getAuthorId());
        sad.setName(new NameDto(a.getFirstName(), a.getSecondName()));

        return sad;
    }
}
