package ua.com.epam.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.exception.entity.IdMismatchException;
import ua.com.epam.exception.entity.author.AuthorAlreadyExistsException;
import ua.com.epam.exception.entity.author.AuthorNotFoundException;
import ua.com.epam.exception.entity.author.BooksInAuthorArePresentException;
import ua.com.epam.exception.entity.book.BookNotFoundException;
import ua.com.epam.exception.entity.genre.GenreNotFoundException;
import ua.com.epam.repository.AuthorRepository;
import ua.com.epam.repository.BookRepository;
import ua.com.epam.repository.GenreRepository;
import ua.com.epam.repository.JsonKeysConformity;
import ua.com.epam.service.mapper.DtoToModelMapper;
import ua.com.epam.service.mapper.ModelToDtoMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

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

    public AuthorDto findAuthorByAuthorId(long authorId) {
        log.debug("Get Author with 'authorId' = " + authorId + "...");
        Optional<Author> exist = authorRepository.getOneByAuthorId(authorId);

        if (!exist.isPresent()) {
            log.error("Author doesn't exist!");
            throw new AuthorNotFoundException(authorId);
        }

        Author toGet = exist.get();
        log.debug("Convert Author to AuthorDto...");
        return toDtoMapper.mapAuthorToAuthorDto(toGet);
    }

    public AuthorDto findAuthorOfBook(long bookId) {
        log.debug("Get Book with 'bookId' = " + bookId + "...");
        if (!bookRepository.existsByBookId(bookId)) {
            log.error("Book with doesn't exist!");
            throw new BookNotFoundException(bookId);
        }

        log.debug("Get Author of Book...");
        Author toGet = authorRepository.getAuthorOfBook(bookId);
        log.debug("Convert Author to AuthorDto...");
        return toDtoMapper.mapAuthorToAuthorDto(toGet);
    }

    public List<AuthorDto> findAllAuthors(String sortBy, String order, int page, int size, boolean pageable) {
        Sort.Direction orderType = getSortDirection(order);
        String sortParam = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        List<Author> authors;

        log.debug("Check pagination...");
        if (pageable) {
            log.debug("Get paginated Authors from page '" + page + "' in '" + size + "' size, sorted by '" + sortBy + "' in '" + orderType + "' order...");
            authors = authorRepository.getAllAuthorsOrderedPaginated(Sort.by(orderType, sortParam), PageRequest.of(page - 1, size));
        } else {
            log.debug("Get all Authors sorted by '" + sortBy + "' in '" + orderType + "' order...");
            authors = authorRepository.getAllAuthorsOrdered(Sort.by(orderType, sortParam));
        }

        log.debug("Convert Author array to AuthorDto array...");
        return authors.stream()
                .map(toDtoMapper::mapAuthorToAuthorDto)
                .collect(Collectors.toList());
    }

    public List<AuthorDto> findAllAuthorsOfGenre(long genreId, String sortBy, String order, int page, int size, boolean pageable) {
        log.debug("Check if Genre with 'genreId' = " + genreId + " already exists...");
        if (!genreRepository.existsByGenreId(genreId)) {
            log.error("Genre doesn't exist!");
            throw new GenreNotFoundException(genreId);
        }

        Sort.Direction orderType = getSortDirection(order);
        String sortParameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        List<Author> authors;

        log.debug("Check pagination...");
        if (pageable) {
            log.debug("Get paginated Authors of Genre with 'genreId' = '" + genreId + "' from page '" + page + "' in '" + size + "' size, sorted by '" + sortBy + "' in '" + orderType + "' order...");
            authors = authorRepository.getAllAuthorsInGenreOrderedPaginated(genreId, Sort.by(orderType, sortParameter), PageRequest.of(page - 1, size));
        } else {
            log.debug("Get paginated Authors of Genre with 'genreId' = '" + genreId + "' sorted by '" + sortBy + "' in '" + orderType + "' order...");
            authors = authorRepository.getAllAuthorsInGenreOrdered(genreId, Sort.by(orderType, sortParameter));
        }

        log.debug("Convert Author array to AuthorDto array...");
        return authors.stream()
                .map(toDtoMapper::mapAuthorToAuthorDto)
                .collect(Collectors.toList());
    }

    public AuthorDto addNewAuthor(AuthorDto author) {
        log.debug("Check if Author with 'authorId' = " + author.getAuthorId() + " already exists...");
        if (authorRepository.existsByAuthorId(author.getAuthorId())) {
            log.error("Author with such 'authorId' already exists!");
            throw new AuthorAlreadyExistsException();
        }

        log.debug("Map AuthorDto to Author...");
        Author toPost = toModelMapper.mapAuthorDtoToAuthor(author);
        log.debug("Insert and save new Author..");
        Author response = authorRepository.save(toPost);
        log.debug("Convert saved Author to AuthorDto...");
        return toDtoMapper.mapAuthorToAuthorDto(response);
    }

    public AuthorDto updateExistedAuthor(long authorId, AuthorDto authorDto) {
        log.debug("Get Author with 'authorId' = " + authorId + "...");
        Optional<Author> opt = authorRepository.getOneByAuthorId(authorId);

        if (!opt.isPresent()) {
            log.error("Author doesn't exist!");
            throw new AuthorNotFoundException(authorId);
        }
        log.error("Check 'authorId' in body is the same as in request URL...");
        if (authorId != authorDto.getAuthorId()) {
            log.error("Id mismatch!");
            throw new IdMismatchException();
        }

        Author proxy = opt.get();

        log.debug("Update Author properties...");
        proxy.setFirstName(authorDto.getAuthorName().getFirst());
        proxy.setSecondName(authorDto.getAuthorName().getSecond());
        proxy.setDescription(authorDto.getAuthorDescription());
        proxy.setNationality(authorDto.getNationality());
        proxy.setBirthDate(authorDto.getBirth().getDate());
        proxy.setBirthCity(authorDto.getBirth().getCity());
        proxy.setBirthCountry(authorDto.getBirth().getCountry());

        log.debug("Update Author...");
        Author updated = authorRepository.save(proxy);

        log.debug("Convert updated Author to AuthorDto...");
        return toDtoMapper.mapAuthorToAuthorDto(updated);
    }

    public AuthorDto deleteExistedAuthor(long authorId, boolean forcibly) {
        log.debug("Get Author with 'authorId' = " + authorId + "...");
        Optional<Author> opt = authorRepository.getOneByAuthorId(authorId);

        if (!opt.isPresent()) {
            log.error("Author doesn't exist!");
            throw new AuthorNotFoundException(authorId);
        }

        Author toDelete = opt.get();
        log.debug("Get books count of Author with 'authorId' = " + authorId + "...");
        long booksCount = bookRepository.getAllAuthorBooks(authorId).size();

        if (booksCount > 0 && !forcibly) {
            log.error("Author has " + booksCount + " books! Cannot be deleted!");
            throw new BooksInAuthorArePresentException(authorId, booksCount);
        }

        log.debug("Delete Author...");
        authorRepository.delete(toDelete);
        return toDtoMapper.mapAuthorToAuthorDto(toDelete);
    }
}
