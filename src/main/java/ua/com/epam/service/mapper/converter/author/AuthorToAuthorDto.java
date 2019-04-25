package ua.com.epam.service.mapper.converter.author;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.nested.BirthDto;
import ua.com.epam.entity.dto.author.nested.NameDto;

public class AuthorToAuthorDto implements Converter<Author, AuthorDto> {

    @Override
    public AuthorDto convert(MappingContext<Author, AuthorDto> mappingContext) {
        Author source = mappingContext.getSource();

        AuthorDto authorDto = new AuthorDto();
        authorDto.setAuthorId(source.getAuthorId());
        authorDto.setAuthorName(new NameDto(source.getFirstName(), source.getSecondName()));
        authorDto.setNationality(source.getNationality());
        authorDto.setBirth(new BirthDto(source.getBirthDate(), source.getBirthCountry(), source.getBirthCity()));
        authorDto.setAuthorDescription(source.getDescription());
        return authorDto;
    }
}
