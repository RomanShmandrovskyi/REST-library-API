package ua.com.api.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.api.entity.Author;
import ua.com.api.entity.Book;
import ua.com.api.entity.Genre;
import ua.com.api.entity.dto.SortByPropertiesDto;
import ua.com.api.entity.dto.book.BookDto;
import ua.com.api.entity.dto.book.BookWithoutIdDto;
import ua.com.api.exception.entity.author.AuthorNotFoundException;
import ua.com.api.exception.entity.book.BookAlreadyExistsException;
import ua.com.api.exception.entity.book.BookNotFoundException;
import ua.com.api.exception.entity.genre.GenreNotFoundException;
import ua.com.api.exception.entity.search.SearchQueryIsBlankException;
import ua.com.api.exception.entity.search.SearchQueryIsTooShortException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BookService extends BaseService {

    private List<BookDto> mapToDto(List<Book> books) {
        return books.stream()
                .map(toDtoMapper::mapBookToBookDto)
                .collect(Collectors.toList());
    }

    public BookDto findBook(long bookId) {
        Book book = bookRepository.getOneByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        return toDtoMapper.mapBookToBookDto(book);
    }

    public List<BookDto> findAllBooks(String sortBy, String order, int page, int size, boolean pageable) {
        String sortParam = convertAndValidateSortBy(sortBy, Book.class);
        Sort.Direction direction = resolveDirection(order);
        Sort sorter = Sort.by(direction, sortParam);

        List<Book> books;

        if (!pageable) {
            books = bookRepository.findAll(sorter);
        } else {
            books = bookRepository.getAllBooks(PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(books);
    }

    public List<BookDto> findBooksInGenre(long genreId, String sortBy, String order, int page, int size, boolean pageable) {
        String sortParam = convertAndValidateSortBy(sortBy, Book.class);

        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        Sort.Direction direction = resolveDirection(order);
        Sort sorter = Sort.by(direction, sortParam);

        List<Book> books;

        if (!pageable) {
            books = bookRepository.findAll(sorter);
        } else {
            books = bookRepository.getAllBooksInGenre(genreId, PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(books);
    }

    public List<BookDto> findAuthorBooks(long authorId, String sortBy, String order) {
        String sortParam = convertAndValidateSortBy(sortBy, Book.class);

        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        Sort.Direction direction = resolveDirection(order);
        Sort sorter = Sort.by(direction, sortParam);

        return mapToDto(bookRepository.getAllAuthorBooksOrdered(authorId, sorter));
    }

    public List<BookDto> findBooksOfAuthorInGenre(long authorId, long genreId) {
        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        return mapToDto(bookRepository.getAllAuthorBooksInGenre(authorId, genreId));
    }

    public List<SortByPropertiesDto> getSortByParameterValues() {
        return getSortByParameterValues(Book.class);
    }

    public List<BookDto> searchForExistedBooks(String searchQuery) {
        List<Book> result = new ArrayList<>();

        searchQuery = searchQuery.trim();
        if (searchQuery.isEmpty()) {
            throw new SearchQueryIsBlankException();
        } else if (searchQuery.length() <= 4) {
            throw new SearchQueryIsTooShortException(searchQuery, 5);
        }

        List<String> splitQuery = Arrays.asList(searchQuery.split(" "));
        List<Book> searched = searchFor.books(searchQuery, splitQuery);

        if (searched.isEmpty()) {
            return new ArrayList<>();
        }

        for (int i = splitQuery.size(); i > 0; i--) {
            String partial = splitQuery.stream()
                    .limit(i)
                    .collect(Collectors.joining(" "));

            List<Book> filtered = searched.stream()
                    .filter(b -> b.getBookName().toLowerCase().startsWith(partial.toLowerCase()))
                    .sorted(Comparator.comparing(Book::getBookName))
                    .toList();

            result.addAll(filtered);

            if (result.size() >= 5) {
                return mapToDto(IntStream.range(0, 5)
                        .mapToObj(result::get)
                        .collect(Collectors.toList()));
            }

            searched.removeAll(filtered);
        }

        result.addAll(searched);

        return mapToDto(result.stream()
                .limit(5)
                .collect(Collectors.toList()));
    }

    public BookDto addNewBook(long authorId, long genreId, BookWithoutIdDto newBook) {
        Author author = authorRepository.getOneByAuthorId(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));
        Genre genre = genreRepository.getOneByGenreId(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId));

        if (bookRepository.existsByBookNameAndBookDescription(newBook.getBookName(), newBook.getBookDescription())) {
            throw new BookAlreadyExistsException();
        }

        Book toPost = toModelMapper.mapBookWithoutIdToBook(newBook);
        toPost.setAuthor(author);
        toPost.setGenre(genre);
        Book response = bookRepository.save(toPost);
        return toDtoMapper.mapBookToBookDto(response);
    }

    public BookDto updateExistedBook(long bookId, BookWithoutIdDto bookDto) {
        Book book = bookRepository.getOneByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if (bookRepository.existsByBookNameAndBookDescriptionAndBookIdNotLike(bookDto.getBookName(), bookDto.getBookDescription(), bookId)) {
            throw new BookAlreadyExistsException();
        }

        book.setBookName(bookDto.getBookName());
        book.setBookLanguage(bookDto.getBookLanguage());
        book.setBookDescription(bookDto.getBookDescription());
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setPagesCount(bookDto.getAdditional().getPagesCount());
        book.setBookWidth(bookDto.getAdditional().getSize().getWidth());
        book.setBookLength(bookDto.getAdditional().getSize().getLength());
        book.setBookHeight(bookDto.getAdditional().getSize().getHeight());

        Book updated = bookRepository.save(book);

        return toDtoMapper.mapBookToBookDto(updated);
    }

    public void deleteExistedBook(long bookId) {
        Book toDelete = bookRepository.getOneByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        bookRepository.delete(toDelete);
    }
}
