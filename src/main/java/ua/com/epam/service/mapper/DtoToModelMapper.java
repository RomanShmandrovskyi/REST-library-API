package ua.com.epam.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.book.BookDto;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.service.mapper.converter.author.AuthorDtoToAuthor;
import ua.com.epam.service.mapper.converter.book.BookDtoToBook;
import ua.com.epam.service.mapper.converter.genre.GenreDtoToGenre;

@Service
public class DtoToModelMapper {
    private ModelMapper modelMapper = new ModelMapper();

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
