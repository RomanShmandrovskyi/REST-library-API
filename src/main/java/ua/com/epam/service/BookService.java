package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.dto.book.BookDto;
import ua.com.epam.exception.entity.author.AuthorNotFoundException;
import ua.com.epam.exception.entity.book.BookAlreadyExistsException;
import ua.com.epam.exception.entity.book.BookNotFoundException;
import ua.com.epam.exception.entity.genre.GenreNotFoundException;
import ua.com.epam.exception.entity.search.SearchQueryIsBlankException;
import ua.com.epam.exception.entity.search.SearchQueryIsTooShortException;
import ua.com.epam.repository.*;
import ua.com.epam.service.mapper.DtoToModelMapper;
import ua.com.epam.service.mapper.ModelToDtoMapper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private SearchFor searchFor;

    @Autowired
    private ModelToDtoMapper toDtoMapper;

    @Autowired
    private DtoToModelMapper toModelMapper;

    private Sort.Direction resolveDirection(String order) {
        return Sort.Direction.fromString(order);
    }

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
        if (sortBy.equalsIgnoreCase("square")) {
            return findAllBooksSortedBySquare(order, page, size, pageable);
        } else if (sortBy.equalsIgnoreCase("volume")) {
            return findAllBooksSortedByVolume(order, page, size, pageable);
        }

        String sortParameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);
        Sort.Direction direction = resolveDirection(order);
        Sort sorter = Sort.by(direction, sortParameter);

        List<Book> books;

        if (!pageable) {
            books = bookRepository.findAll(sorter);
        } else {
            books = bookRepository.getAllBooks(PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(books);
    }

    private List<BookDto> findAllBooksSortedBySquare(String order, int page, int size, boolean pageable) {
        Sort.Direction direction = resolveDirection(order);
        Sort sorter = Sort.by(direction, "square");

        List<Book> books;

        if (!pageable) {
            books = bookRepository.findAll(sorter);
        } else {
            books = bookRepository.getAllBooks(PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(books);
    }

    private List<BookDto> findAllBooksSortedByVolume(String order, int page, int size, boolean pageable) {
        Sort.Direction direction = resolveDirection(order);
        Sort sorter = Sort.by(direction, "volume");

        List<Book> books;

        if (!pageable) {
            books = bookRepository.findAll(sorter);
        } else {
            books = bookRepository.getAllBooks(PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(books);
    }

    public List<BookDto> findBooksInGenre(long genreId, String sortBy, String order, int page, int size, boolean pageable) {
        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        Sort.Direction direction = resolveDirection(order);
        String sortParameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);
        Sort sorter = Sort.by(direction, sortParameter);

        List<Book> books;

        if (!pageable) {
            books = bookRepository.findAll(sorter);
        } else {
            books = bookRepository.getAllBooksInGenre(genreId, PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(books);
    }

    public List<BookDto> findAuthorBooks(long authorId, String sortBy, String order) {
        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        Sort.Direction direction = resolveDirection(order);
        String sortParameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);
        Sort sorter = Sort.by(direction, sortParameter);

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
                    .sorted(Comparator.comparing(Book::getBookName)).collect(Collectors.toList());

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

    public BookDto updateExistedBook(BookDto bookDto) {
        Optional<Book> opt = bookRepository.getOneByBookId(bookDto.getBookId());

        if (!opt.isPresent()) {
            throw new BookNotFoundException(bookDto.getBookId());
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
