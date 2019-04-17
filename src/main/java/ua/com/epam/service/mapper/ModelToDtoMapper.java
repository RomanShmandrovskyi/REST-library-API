package ua.com.epam.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.service.mapper.converter.AuthorToAuthorGetDto;

@Service
public class ModelToDtoMapper {

    private ModelMapper modelMapper = new ModelMapper();

    public ModelToDtoMapper() {
        modelMapper.addConverter(new AuthorToAuthorGetDto());
    }

    public AuthorGetDto mapAuthorModelToGetDto(Author author) {
        return modelMapper.map(author, AuthorGetDto.class);
    }
}
