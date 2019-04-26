package ua.com.epam.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.GroupByBooksCount;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.AuthorGroupByBooksDto;
import ua.com.epam.entity.dto.author.SimpleAuthorDto;
import ua.com.epam.entity.dto.book.BookDto;
import ua.com.epam.entity.dto.book.BookWithAuthorAndGenreDto;
import ua.com.epam.entity.dto.book.GenreGroupByBooksDto;
import ua.com.epam.entity.dto.book.nested.AdditionalDto;
import ua.com.epam.entity.dto.book.nested.SizeDto;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.entity.dto.genre.SimpleGenreDto;
import ua.com.epam.service.mapper.converter.author.AuthorToAuthorDto;
import ua.com.epam.service.mapper.converter.author.AuthorToSimpleAuthorDto;
import ua.com.epam.service.mapper.converter.book.BookToBookDto;
import ua.com.epam.service.mapper.converter.genre.GenreToGenreDto;
import ua.com.epam.service.mapper.converter.genre.GenreToSimpleGenreDto;
import ua.com.epam.service.mapper.converter.group.GroupByBooksToAuthor;
import ua.com.epam.service.mapper.converter.group.GroupByBooksToGenre;

@Service
public class ModelToDtoMapper {

    private ModelMapper modelMapper = new ModelMapper();

    public ModelToDtoMapper() {
        modelMapper.addConverter(new AuthorToAuthorDto());
        modelMapper.addConverter(new AuthorToSimpleAuthorDto());

        modelMapper.addConverter(new GenreToGenreDto());
        modelMapper.addConverter(new GenreToSimpleGenreDto());

        modelMapper.addConverter(new BookToBookDto());

        modelMapper.addConverter(new GroupByBooksToAuthor());
        modelMapper.addConverter(new GroupByBooksToGenre());
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

    public AuthorGroupByBooksDto mapGroupModelToAuthorGroup(GroupByBooksCount group) {
        return modelMapper.map(group, AuthorGroupByBooksDto.class);
    }

    public GenreGroupByBooksDto mapGroupModelToGenreGroup(GroupByBooksCount group) {
        return modelMapper.map(group, GenreGroupByBooksDto.class);
    }

    public BookWithAuthorAndGenreDto getBookWithAuthorAndGenreDto(Book book, Author author, Genre genre) {
        BookWithAuthorAndGenreDto bookWithAuthorAndGenreDto = new BookWithAuthorAndGenreDto();

        bookWithAuthorAndGenreDto.setBookId(book.getBookId());
        bookWithAuthorAndGenreDto.setBookName(book.getBookName());
        bookWithAuthorAndGenreDto.setBookLanguage(book.getBookLang());

        AdditionalDto additional = new AdditionalDto();
        additional.setPageCount(book.getPageCount());
        additional.setSize(new SizeDto(book.getBookHeight(), book.getBookWidth(), book.getBookLength()));

        bookWithAuthorAndGenreDto.setAdditional(additional);
        bookWithAuthorAndGenreDto.setPublicationYear(book.getPublicationYear());

        SimpleAuthorDto simpleAuthor = modelMapper.map(author, SimpleAuthorDto.class);
        bookWithAuthorAndGenreDto.setAuthor(simpleAuthor);

        SimpleGenreDto simpleGenre = modelMapper.map(genre, SimpleGenreDto.class);
        bookWithAuthorAndGenreDto.setGenre(simpleGenre);

        return bookWithAuthorAndGenreDto;
    }
}
