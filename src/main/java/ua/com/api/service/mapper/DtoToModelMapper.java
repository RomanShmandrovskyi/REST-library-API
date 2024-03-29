package ua.com.api.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.api.entity.Author;
import ua.com.api.entity.Book;
import ua.com.api.entity.Genre;
import ua.com.api.entity.dto.author.AuthorWithoutIdDto;
import ua.com.api.entity.dto.book.BookWithoutIdDto;
import ua.com.api.entity.dto.genre.GenreWithoutIdDto;
import ua.com.api.service.mapper.converter.author.AuthorWithoutIdDto_to_Author;
import ua.com.api.service.mapper.converter.book.BookWithoutIdDto_to_Book;
import ua.com.api.service.mapper.converter.genre.GenreWithoutIdDto_to_Genre;

@Service
public class DtoToModelMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public DtoToModelMapper() {
        modelMapper.addConverter(new AuthorWithoutIdDto_to_Author());
        modelMapper.addConverter(new GenreWithoutIdDto_to_Genre());
        modelMapper.addConverter(new BookWithoutIdDto_to_Book());
    }

    public Author mapAuthorWithoutIdDtoToAuthor(AuthorWithoutIdDto author) {
        return modelMapper.map(author, Author.class);
    }

    public Genre mapGenreWithoutIdToGenre(GenreWithoutIdDto genre) {
        return modelMapper.map(genre, Genre.class);
    }

    public Book mapBookWithoutIdToBook(BookWithoutIdDto book) {
        return modelMapper.map(book, Book.class);
    }
}
