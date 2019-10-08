package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.exception.entity.IdMismatchException;
import ua.com.epam.exception.entity.search.SearchQueryIsBlankException;
import ua.com.epam.exception.entity.search.SearchQueryIsTooShortException;
import ua.com.epam.exception.entity.author.AuthorAlreadyExistsException;
import ua.com.epam.exception.entity.author.AuthorNotFoundException;
import ua.com.epam.exception.entity.author.BooksInAuthorArePresentException;
import ua.com.epam.exception.entity.book.BookNotFoundException;
import ua.com.epam.exception.entity.genre.GenreNotFoundException;
import ua.com.epam.repository.*;
import ua.com.epam.service.mapper.DtoToModelMapper;
import ua.com.epam.service.mapper.ModelToDtoMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SearchFor searchFor;

    @Autowired
    private ModelToDtoMapper toDtoMapper;

    @Autowired
    private DtoToModelMapper toModelMapper;

    private Sort.Direction resolveDirection(String order) {
        return Sort.Direction.fromString(order);
    }

    private List<AuthorDto> mapToDto(List<Author> authors) {
        return authors.stream()
                .map(toDtoMapper::mapAuthorToAuthorDto)
                .collect(Collectors.toList());
    }

    public AuthorDto findAuthor(long authorId) {
        Author toGet = authorRepository.getOneByAuthorId(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        return toDtoMapper.mapAuthorToAuthorDto(toGet);
    }

    public AuthorDto findAuthorOfBook(long bookId) {
        if (!bookRepository.existsByBookId(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        Author toGet = authorRepository.getAuthorOfBook(bookId);

        return toDtoMapper.mapAuthorToAuthorDto(toGet);
    }

    public List<AuthorDto> findAllAuthors(String sortBy, String order, int page, int size, boolean pageable) {
        Sort.Direction direction = resolveDirection(order);
        String sortParam = JsonKeysConformity.getPropNameByJsonKey(sortBy);
        Sort sorter = Sort.by(direction, sortParam);

        List<Author> authors;

        if (!pageable) {
            authors = authorRepository.findAll(sorter);
        } else {
            authors = authorRepository
                    .getAllAuthorsOrderedPaginated(PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(authors);
    }

    public List<AuthorDto> searchForExistedAuthors(String searchQuery) {
        String searchQueryTrimmed = searchQuery.trim();

        if (searchQueryTrimmed.isEmpty()) {
            throw new SearchQueryIsBlankException();
        }

        if (searchQueryTrimmed.length() <= 2) {
            throw new SearchQueryIsTooShortException(searchQueryTrimmed, 3);
        }

        List<String> keywords = Arrays.stream(searchQuery.split(" "))
                .filter(e -> e.length() > 2)
                .collect(Collectors.toList());

        List<Author> searched = searchFor.authors(searchQuery, keywords);

        return mapToDto(searched);
    }

    public List<AuthorDto> findAllAuthorsInGenre(long genreId, String sortBy, String order, int page, int size, boolean pageable) {
        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        Sort.Direction direction = resolveDirection(order);
        String sortParam = JsonKeysConformity.getPropNameByJsonKey(sortBy);
        Sort sorter = Sort.by(direction, sortParam);

        List<Author> authors;

        if (!pageable) {
            authors = authorRepository.findAll(sorter);
        } else {
            authors = authorRepository
                    .getAllAuthorsInGenreOrderedPaginated(genreId, PageRequest.of(page - 1, size, sorter));
        }

        return mapToDto(authors);
    }

    public AuthorDto addNewAuthor(AuthorDto author) {
        if (authorRepository.existsByAuthorId(author.getAuthorId())) {
            throw new AuthorAlreadyExistsException();
        }

        Author toPost = toModelMapper.mapAuthorDtoToAuthor(author);
        Author response = authorRepository.save(toPost);

        return toDtoMapper.mapAuthorToAuthorDto(response);
    }

    public AuthorDto updateExistedAuthor(long authorId, AuthorDto authorDto) {
        Optional<Author> opt = authorRepository.getOneByAuthorId(authorId);

        if (!opt.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        if (authorId != authorDto.getAuthorId()) {
            throw new IdMismatchException();
        }

        Author proxy = opt.get();

        proxy.setFirstName(authorDto.getAuthorName().getFirst());
        proxy.setSecondName(authorDto.getAuthorName().getSecond());
        proxy.setDescription(authorDto.getAuthorDescription());
        proxy.setNationality(authorDto.getNationality());
        proxy.setBirthDate(authorDto.getBirth().getDate());
        proxy.setBirthCity(authorDto.getBirth().getCity());
        proxy.setBirthCountry(authorDto.getBirth().getCountry());

        Author updated = authorRepository.save(proxy);

        return toDtoMapper.mapAuthorToAuthorDto(updated);
    }

    public void deleteExistedAuthor(long authorId, boolean forcibly) {
        Author toDelete = authorRepository.getOneByAuthorId(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        long booksCount = bookRepository.getAllBooksOfAuthorCount(authorId);

        if (booksCount > 0 && !forcibly) {
            throw new BooksInAuthorArePresentException(authorId, booksCount);
        }

        authorRepository.delete(toDelete);
    }
}