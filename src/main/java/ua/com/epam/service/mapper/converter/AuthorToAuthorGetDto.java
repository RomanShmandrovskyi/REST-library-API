package ua.com.epam.service.mapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.entity.dto.author.BirthDto;
import ua.com.epam.entity.dto.author.NameDto;

public class AuthorToAuthorGetDto implements Converter<Author, AuthorGetDto> {

    @Override
    public AuthorGetDto convert(MappingContext<Author, AuthorGetDto> mappingContext) {
        Author source = mappingContext.getSource();

        AuthorGetDto authorDto = new AuthorGetDto();
        authorDto.setAuthorId(source.getAuthorId());
        authorDto.setAuthorName(new NameDto(source.getFirstName(), source.getSecondName()));
        authorDto.setNationality(source.getNationality());
        authorDto.setBirth(new BirthDto(source.getBirthDate(), source.getBirthCountry(), source.getBirthCity()));
        authorDto.setDescription(source.getDescription());
        authorDto.setBooksCount(source.getBooksCount());
        return authorDto;
    }
}
