package ua.com.api.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.api.entity.Author;
import ua.com.api.entity.Book;
import ua.com.api.entity.Genre;
import ua.com.api.entity.dto.author.AuthorDto;
import ua.com.api.entity.dto.book.BookDto;
import ua.com.api.entity.dto.genre.GenreDto;
import ua.com.api.service.mapper.converter.author.AuthorDtoToAuthor;
import ua.com.api.service.mapper.converter.book.BookDtoToBook;
import ua.com.api.service.mapper.converter.genre.GenreDtoToGenre;

@Service
public class DtoToModelMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public DtoToModelMapper() {
        modelMapper.addConverter(new AuthorDtoToAuthor());
        modelMapper.addConverter(new GenreDtoToGenre());
        modelMapper.addConverter(new BookDtoToBook());
    }

    public Author mapAuthorDtoToAuthor(AuthorDto author) {
        return modelMapper.map(author, Author.class);
    }

    public Genre mapGenreDtoToGenre(GenreDto genre) {
        return modelMapper.map(genre, Genre.class);
    }

    public Book mapBookDtoToBook(BookDto book) {
        return modelMapper.map(book, Book.class);
    }
}
