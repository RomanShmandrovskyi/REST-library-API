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
import ua.com.epam.exception.entity.IdMismatchException;
import ua.com.epam.exception.entity.author.AuthorNotFoundException;
import ua.com.epam.exception.entity.book.BookAlreadyExistsException;
import ua.com.epam.exception.entity.book.BookNotFoundException;
import ua.com.epam.exception.entity.genre.GenreNotFoundException;
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
        Book book = bookRepository.getOneByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        return toDtoMapper.mapBookToBookDto(book);
    }

    public BookWithAuthorAndGenreDto findBookWithAuthorAndGenreInfo(long bookId) {
        Book book = bookRepository.getOneByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        Author authorOfBook = authorRepository.getAuthorOfBook(bookId);
        Genre genreOfBook = genreRepository.getGenreOfBook(bookId);

        return toDtoMapper.getBookWithAuthorAndGenreDto(book, authorOfBook, genreOfBook);
    }

    public List<BookDto> findAllBooks(String sortBy, String order, int page, int size, boolean pageable) {
        String sortParameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);
        Sort.Direction orderType = getSortDirection(order);

        List<Book> books = pageable ?
                bookRepository.getAllBooksOrderedPaginated(Sort.by(orderType, sortParameter), PageRequest.of(page - 1, size)) :
                bookRepository.getAllBooksOrdered(Sort.by(orderType, sortParameter));

        return books.stream()
                .map(toDtoMapper::mapBookToBookDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findAllBooksSortedByDimension(String dimension, int page, int size, boolean pagination) {
        List<Book> books;

        if (dimension.equals("volume")) {
            books = pagination ?
                    bookRepository.getAllBooksOrderedByVolumePaginated(PageRequest.of(page - 1, size)) :
                    bookRepository.getAllBooksOrderedByVolume();
        } else {
            books = pagination ?
                    bookRepository.getAllBooksOrderedBySquarePaginated(PageRequest.of(page - 1, size)) :
                    bookRepository.getAllBooksOrderedBySquare();
        }

        return books.stream()
                .map(toDtoMapper::mapBookToBookDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksInGenre(long genreId, String sortBy, String order, int page, int size, boolean pageable) {
        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        Sort.Direction orderType = getSortDirection(order);
        String sortParameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        List<Book> books = pageable ?
                bookRepository.getAllBooksInGenreOrderedPaginated(genreId, Sort.by(orderType, sortParameter), PageRequest.of(page - 1, size)) :
                bookRepository.getAllBooksInGenreOrdered(genreId, Sort.by(orderType, sortParameter));

        return books.stream()
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

    public BookDto addNewBook(long authorId, long genreId, BookDto newBook) {
        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        if (bookRepository.existsByBookId(newBook.getBookId())) {
            throw new BookAlreadyExistsException();
        }

        Book toPost = toModelMapper.mapBookDtoToBook(newBook);
        toPost.setAuthorId(authorId);
        toPost.setGenreId(genreId);

        Book response = bookRepository.save(toPost);

        return toDtoMapper.mapBookToBookDto(response);
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

    public void deleteExistedBook(long bookId) {
        Book toDelete = bookRepository.getOneByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        bookRepository.delete(toDelete);
    }
}
