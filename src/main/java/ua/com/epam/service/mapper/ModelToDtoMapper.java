package ua.com.epam.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.entity.dto.book.AuthorBookDto;
import ua.com.epam.entity.dto.genre.AuthorGenreDto;
import ua.com.epam.service.mapper.converter.AuthorToAuthorGetDto;
import ua.com.epam.service.mapper.converter.BookToAuthorBookDto;
import ua.com.epam.service.mapper.converter.GenreToAuthorGenreDto;

@Service
public class ModelToDtoMapper {

    private ModelMapper modelMapper = new ModelMapper();

    public ModelToDtoMapper() {
        modelMapper.addConverter(new AuthorToAuthorGetDto());
        modelMapper.addConverter(new GenreToAuthorGenreDto());
        modelMapper.addConverter(new BookToAuthorBookDto());
    }

    public AuthorGetDto mapAuthorModelToGetDto(Author author) {
        return modelMapper.map(author, AuthorGetDto.class);
    }

    public AuthorGenreDto mapGenreToAuthorGenreDto(Genre genre) {
        return modelMapper.map(genre, AuthorGenreDto.class);
    }

    public AuthorBookDto mapBookToAuthorBookDto(Book book) {
        return modelMapper.map(book, AuthorBookDto.class);
    }
}
