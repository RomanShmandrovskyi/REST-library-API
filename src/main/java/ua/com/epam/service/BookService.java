package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.book.BookDto;
import ua.com.epam.entity.dto.book.BookWithAuthorAndGenreDto;
import ua.com.epam.entity.exception.IdMismatchException;
import ua.com.epam.entity.exception.author.AuthorNotFoundException;
import ua.com.epam.entity.exception.book.BookAlreadyExistsException;
import ua.com.epam.entity.exception.book.BookNotFoundException;
import ua.com.epam.entity.exception.genre.GenreNotFoundException;
import ua.com.epam.repository.*;
import ua.com.epam.service.mapper.DtoToModelMapper;
import ua.com.epam.service.mapper.ModelToDtoMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ModelToDtoMapper toDtoMapper;

    @Autowired
    private DtoToModelMapper toModelMapper;

    private Sort.Direction getSortDirection(String order) {
        Sort.Direction orderType = null;

        if (order.equals("desc"))
            orderType = Sort.Direction.DESC;
        else if (order.equals("asc"))
            orderType = Sort.Direction.ASC;

        return orderType;
    }

    public BookDto findBookByBookId(long bookId) {
        Optional<Book> opt = bookRepository.getOneByBookId(bookId);

        if (!opt.isPresent()) {
            throw new BookNotFoundException(bookId);
        }

        Book book = opt.get();
        return toDtoMapper.mapBookToBookDto(book);
    }

    public BookWithAuthorAndGenreDto findBookWithAuthorAndGenreInfo(long bookId) {
        Optional<Book> opt = bookRepository.getOneByBookId(bookId);

        if (!opt.isPresent()) {
            throw new BookNotFoundException(bookId);
        }

        Book book = opt.get();
        Author authorOfBook = authorRepository.getAuthorOfBook(bookId);
        Genre genreOfBook = genreRepository.getGenreOfBook(bookId);

        return toDtoMapper.getBookWithAuthorAndGenreDto(book, authorOfBook, genreOfBook);
    }

    public List<BookDto> findAllBooks(String sortBy, String order, int page, int size, boolean pageable) {
        Sort.Direction orderType = getSortDirection(order);
        String sortParameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        if (pageable) {
            return bookRepository
                    .getAllBooksOrderedPaginated(Sort.by(orderType, sortParameter), PageRequest.of(page, size))
                    .stream()
                    .map(toDtoMapper::mapBookToBookDto)
                    .collect(Collectors.toList());
        }

        return bookRepository
                .getAllBooksOrdered(Sort.by(orderType, sortParameter))
                .stream()
                .map(toDtoMapper::mapBookToBookDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksInGenre(long genreId, String sortBy, String order, int page, int size, boolean pageable) {
        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        Sort.Direction orderType = getSortDirection(order);
        String sortParameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        if (pageable) {
            return bookRepository
                    .getAllBooksInGenreOrderedPaginated(genreId, Sort.by(orderType, sortParameter), PageRequest.of(page, size))
                    .stream()
                    .map(toDtoMapper::mapBookToBookDto)
                    .collect(Collectors.toList());
        }

        return bookRepository
                .getAllBooksInGenreOrdered(genreId, Sort.by(orderType, sortParameter))
                .stream()
                .map(toDtoMapper::mapBookToBookDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findAuthorBooks(long authorId, String sortBy, String order) {
        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        Sort.Direction orderType = getSortDirection(order);
        String sortParameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        return bookRepository
                .getAllAuthorBooksOrdered(authorId, Sort.by(orderType, sortParameter))
                .stream()
                .map(toDtoMapper::mapBookToBookDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksOfAuthorInGenre(long authorId, long genreId) {
        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        return bookRepository
                .getAllAuthorBooksInGenre(authorId, genreId)
                .stream()
                .map(toDtoMapper::mapBookToBookDto)
                .collect(Collectors.toList());
    }

    public BookWithAuthorAndGenreDto addNewBook(long authorId, long genreId, BookDto newBook) {
        Optional<Author> optA = authorRepository.getOneByAuthorId(authorId);
        if (!optA.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Optional<Genre> optG = genreRepository.getOneByGenreId(genreId);
        if (!optG.isPresent()) {
            throw new GenreNotFoundException(genreId);
        }

        if (bookRepository.existsByBookId(newBook.getBookId())) {
            throw new BookAlreadyExistsException();
        }

        Book toPost = toModelMapper.mapBookDtoToBook(newBook);
        toPost.setAuthorId(authorId);
        toPost.setGenreId(genreId);

        Book response = bookRepository.save(toPost);

        return toDtoMapper.getBookWithAuthorAndGenreDto(response, optA.get(), optG.get());
    }

    public BookDto updateExistedBook(long bookId, BookDto bookDto) {
        Optional<Book> opt = bookRepository.getOneByBookId(bookId);

        if (!opt.isPresent()) {
            throw new BookNotFoundException(bookId);
        }
        if (bookId != bookDto.getBookId()) {
            throw new IdMismatchException();
        }

        Book proxy = opt.get();

        proxy.setBookName(bookDto.getBookName());
        proxy.setBookLang(bookDto.getBookLanguage());
        proxy.setDescription(bookDto.getBookDescription());
        proxy.setPublicationYear(bookDto.getPublicationYear());
        proxy.setPageCount(bookDto.getAdditional().getPageCount());
        proxy.setBookWidth(bookDto.getAdditional().getSize().getWidth());
        proxy.setBookLength(bookDto.getAdditional().getSize().getLength());
        proxy.setBookHeight(bookDto.getAdditional().getSize().getHeight());

        Book updated = bookRepository.save(proxy);

        return toDtoMapper.mapBookToBookDto(updated);
    }

    public BookDto deleteExistedBook(long bookId) {
        Optional<Book> opt = bookRepository.getOneByBookId(bookId);

        if (!opt.isPresent()) {
            throw new BookNotFoundException(bookId);
        }

        Book toDelete = opt.get();
        bookRepository.delete(toDelete);

        return toDtoMapper.mapBookToBookDto(toDelete);
    }
}
