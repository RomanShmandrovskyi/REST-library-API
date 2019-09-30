package ua.com.epam.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.book.BookDto;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.service.mapper.converter.author.AuthorToAuthorDto;
import ua.com.epam.service.mapper.converter.book.BookToBookDto;
import ua.com.epam.service.mapper.converter.genre.GenreToGenreDto;

@Service
public class ModelToDtoMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public ModelToDtoMapper() {
        modelMapper.addConverter(new AuthorToAuthorDto());
        modelMapper.addConverter(new GenreToGenreDto());
        modelMapper.addConverter(new BookToBookDto());
    }

    public AuthorDto mapAuthorToAuthorDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    public GenreDto mapGenreToGenreDto(Genre genre) {
        return modelMapper.map(genre, GenreDto.class);
    }

    public BookDto mapBookToBookDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }
}
