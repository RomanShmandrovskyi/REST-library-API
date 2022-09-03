package ua.com.api.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.api.entity.Author;
import ua.com.api.entity.Book;
import ua.com.api.entity.Genre;
import ua.com.api.entity.dto.author.AuthorDto;
import ua.com.api.entity.dto.book.BookDto;
import ua.com.api.entity.dto.genre.GenreDto;
import ua.com.api.service.mapper.converter.author.Author_to_AuthorDto;
import ua.com.api.service.mapper.converter.book.Book_to_BookDto;
import ua.com.api.service.mapper.converter.genre.Genre_to_GenreDto;

@Service
public class ModelToDtoMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public ModelToDtoMapper() {
        modelMapper.addConverter(new Author_to_AuthorDto());
        modelMapper.addConverter(new Genre_to_GenreDto());
        modelMapper.addConverter(new Book_to_BookDto());
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
