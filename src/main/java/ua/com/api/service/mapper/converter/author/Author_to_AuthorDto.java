package ua.com.api.service.mapper.converter.author;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.api.entity.Author;
import ua.com.api.entity.dto.author.AuthorDto;
import ua.com.api.entity.dto.author.nested.BirthDto;
import ua.com.api.entity.dto.author.nested.NameDto;

public class Author_to_AuthorDto implements Converter<Author, AuthorDto> {

    @Override
    public AuthorDto convert(MappingContext<Author, AuthorDto> mappingContext) {
        Author source = mappingContext.getSource();

        AuthorDto authorDto = new AuthorDto();
        authorDto.setAuthorId(source.getAuthorId());
        authorDto.setName(new NameDto(source.getFirstName(), source.getLastName()));
        authorDto.setNationality(source.getNationality());
        authorDto.setBirth(new BirthDto(source.getBirthDate(), source.getBirthCountry(), source.getBirthCity()));
        authorDto.setDescription(source.getDescription());
        return authorDto;
    }
}
