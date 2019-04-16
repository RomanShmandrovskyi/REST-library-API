package ua.com.epam.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorPostDto;
import ua.com.epam.service.mapper.converter.AuthorPostDtoToAuthor;

@Service
public class DtoToModelMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public DtoToModelMapper() {
        modelMapper.addConverter(new AuthorPostDtoToAuthor());
    }

    public Author mapAuthorDtoToAuthor(AuthorPostDto author) {
        return modelMapper.map(author, Author.class);
    }
}
