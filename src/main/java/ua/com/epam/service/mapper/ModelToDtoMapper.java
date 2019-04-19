package ua.com.epam.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.SimpleAuthorDto;
import ua.com.epam.entity.dto.book.SimpleBookDto;
import ua.com.epam.entity.dto.genre.SimpleGenreDto;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.service.mapper.converter.author.AuthorToAuthorDto;
import ua.com.epam.service.mapper.converter.author.AuthorToSimpleAuthorDto;
import ua.com.epam.service.mapper.converter.book.BookToSimpleBookDto;
import ua.com.epam.service.mapper.converter.genre.GenreToSimpleGenreDto;
import ua.com.epam.service.mapper.converter.genre.GenreToGenreDto;

@Service
public class ModelToDtoMapper {

    private ModelMapper modelMapper = new ModelMapper();

    public ModelToDtoMapper() {
        modelMapper.addConverter(new AuthorToAuthorDto());
        modelMapper.addConverter(new GenreToSimpleGenreDto());
        modelMapper.addConverter(new BookToSimpleBookDto());
        modelMapper.addConverter(new GenreToGenreDto());
        modelMapper.addConverter(new AuthorToSimpleAuthorDto());
    }

    public AuthorDto mapAuthorToAuthorDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    public SimpleAuthorDto mapAuthorToSimpleAuthorDto(Author author) {
        return modelMapper.map(author, SimpleAuthorDto.class);
    }

    public SimpleGenreDto mapGenreToSimpleGenreDto(Genre genre) {
        return modelMapper.map(genre, SimpleGenreDto.class);
    }

    public SimpleBookDto mapBookToSimpleBookDto(Book book) {
        return modelMapper.map(book, SimpleBookDto.class);
    }

    public GenreDto mapGenreToGenreDto(Genre genre) {
        return modelMapper.map(genre, GenreDto.class);
    }
}
