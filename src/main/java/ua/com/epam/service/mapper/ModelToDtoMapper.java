package ua.com.epam.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.SimpleAuthorDto;
import ua.com.epam.entity.dto.author.SimpleAuthorWithBooksDto;
import ua.com.epam.entity.dto.author.SimpleAuthorWithGenresDto;
import ua.com.epam.entity.dto.author.nested.NameDto;
import ua.com.epam.entity.dto.book.BookDto;
import ua.com.epam.entity.dto.book.BookWithAuthorAndGenreSimpleDto;
import ua.com.epam.entity.dto.book.SimpleBookDto;
import ua.com.epam.entity.dto.book.nested.AdditionalDto;
import ua.com.epam.entity.dto.book.nested.SizeDto;
import ua.com.epam.entity.dto.genre.SimpleGenreDto;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.entity.dto.genre.SimpleGenreWithAuthorsDto;
import ua.com.epam.entity.dto.genre.SimpleGenreWithBooksDto;
import ua.com.epam.service.mapper.converter.author.AuthorToAuthorDto;
import ua.com.epam.service.mapper.converter.author.AuthorToSimpleAuthorDto;
import ua.com.epam.service.mapper.converter.book.BookToBookDto;
import ua.com.epam.service.mapper.converter.book.BookToSimpleBookDto;
import ua.com.epam.service.mapper.converter.genre.GenreToSimpleGenreDto;
import ua.com.epam.service.mapper.converter.genre.GenreToGenreDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelToDtoMapper {

    private ModelMapper modelMapper = new ModelMapper();

    public ModelToDtoMapper() {
        modelMapper.addConverter(new AuthorToAuthorDto());
        modelMapper.addConverter(new AuthorToSimpleAuthorDto());

        modelMapper.addConverter(new GenreToGenreDto());
        modelMapper.addConverter(new GenreToSimpleGenreDto());

        modelMapper.addConverter(new BookToBookDto());
        modelMapper.addConverter(new BookToSimpleBookDto());
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

    public SimpleAuthorWithGenresDto getSimpleAuthorWithGenresDto(Author author, List<Genre> authorGenres) {
        SimpleAuthorWithGenresDto authorWithGenres = new SimpleAuthorWithGenresDto();

        List<SimpleGenreDto> simpleGenres = authorGenres.stream()
                .map(g -> modelMapper.map(g, SimpleGenreDto.class))
                .collect(Collectors.toList());

        authorWithGenres.setAuthorId(author.getAuthorId());
        authorWithGenres.setAuthorName(new NameDto(author.getFirstName(), author.getSecondName()));
        authorWithGenres.setGenres(simpleGenres);

        return authorWithGenres;
    }

    public SimpleAuthorWithBooksDto getSimpleAuthorWithBooksDto(Author author, List<Book> authorBooks) {
        SimpleAuthorWithBooksDto authorWithBooksDto = new SimpleAuthorWithBooksDto();

        List<SimpleBookDto> simpleBooks = authorBooks.stream()
                .map(b -> modelMapper.map(b, SimpleBookDto.class))
                .collect(Collectors.toList());

        authorWithBooksDto.setAuthorId(author.getAuthorId());
        authorWithBooksDto.setAuthorName(new NameDto(author.getFirstName(), author.getSecondName()));
        authorWithBooksDto.setBooks(simpleBooks);

        return authorWithBooksDto;
    }

    public SimpleGenreWithAuthorsDto getSimpleGenreWithAuthorsDto(Genre genre, List<Author> authorsInGenre) {
        SimpleGenreWithAuthorsDto genreWithAuthorsDto = new SimpleGenreWithAuthorsDto();

        List<SimpleAuthorDto> simpleAuthors = authorsInGenre.stream()
                .map(a -> modelMapper.map(a, SimpleAuthorDto.class))
                .collect(Collectors.toList());

        genreWithAuthorsDto.setGenreId(genre.getGenreId());
        genreWithAuthorsDto.setGenreName(genre.getGenreName());
        genreWithAuthorsDto.setAuthors(simpleAuthors);

        return genreWithAuthorsDto;
    }

    public SimpleGenreWithBooksDto getSimpleGenreWithBooksDto(Genre genre, List<Book> booksInGenre) {
        SimpleGenreWithBooksDto genreWithBooksDto = new SimpleGenreWithBooksDto();

        List<SimpleBookDto> simpleBooks = booksInGenre.stream()
                .map(b -> modelMapper.map(b, SimpleBookDto.class))
                .collect(Collectors.toList());

        genreWithBooksDto.setGenreId(genre.getGenreId());
        genreWithBooksDto.setGenreName(genre.getGenreName());
        genreWithBooksDto.setBooks(simpleBooks);

        return genreWithBooksDto;
    }

    public BookWithAuthorAndGenreSimpleDto getBookWithAuthorAndGenreDto(Book book, Author author, Genre genre) {
        BookWithAuthorAndGenreSimpleDto bookWithAuthorAndGenreDto = new BookWithAuthorAndGenreSimpleDto();

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
