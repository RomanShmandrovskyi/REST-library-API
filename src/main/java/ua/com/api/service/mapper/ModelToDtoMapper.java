package ua.com.api.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.api.entity.Author;
import ua.com.api.entity.Book;
import ua.com.api.entity.Genre;
import ua.com.api.entity.dto.author.AuthorDto;
import ua.com.api.entity.dto.book.BookDto;
import ua.com.api.entity.dto.genre.GenreDto;
import ua.com.api.service.mapper.converter.author.AuthorToAuthorDto;
import ua.com.api.service.mapper.converter.book.BookToBookDto;
import ua.com.api.service.mapper.converter.genre.GenreToGenreDto;

@Service
public class ModelToDtoMapper {
    private final ModelMapper modelMapper = new ModelMapper();

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
