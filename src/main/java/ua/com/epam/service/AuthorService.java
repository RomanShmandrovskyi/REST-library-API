package ua.com.epam.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.com.epam.repository.JsonKeysConformity.AUTHOR_ID;

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

    private Sort.Direction resolveDirection(String order) {
        Sort.Direction orderType = null;

        if (order.equals("desc"))
            orderType = Sort.Direction.DESC;
        else if (order.equals("asc"))
            orderType = Sort.Direction.ASC;

        return orderType;
    }

    public AuthorDto findAuthorByAuthorId(long authorId) {
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
        Sort.Direction orderType = resolveDirection(order);
        String sortParam = JsonKeysConformity.getPropNameByJsonKey(sortBy);
        List<Author> authors;

        if (!pageable) {
            int authorsCount = (int) authorRepository.count();
            authors = authorRepository.getAllAuthorsOrderedPaginated(PageRequest.of(0, authorsCount, Sort.by(orderType, sortParam)));
        } else {
            authors =  authorRepository.getAllAuthorsOrderedPaginated(PageRequest.of(page - 1, size, Sort.by(orderType, sortParam)));
        }

        return authors.stream()
                .map(toDtoMapper::mapAuthorToAuthorDto)
                .collect(Collectors.toList());
    }

    public List<AuthorDto> findAllAuthorsOfGenre(long genreId, String sortBy, String order, int page, int size, boolean pageable) {
        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        Sort.Direction orderType = resolveDirection(order);
        String sortParam = JsonKeysConformity.getPropNameByJsonKey(sortBy);
        List<Author> authors;

        if (!pageable) {
            int authorsCount = (int) authorRepository.count();
            authors = authorRepository.getAllAuthorsInGenreOrderedPaginated(genreId, PageRequest.of(0, authorsCount, Sort.by(orderType, sortParam)));
        } else {
            authors = authorRepository.getAllAuthorsInGenreOrderedPaginated(genreId, PageRequest.of(page - 1, size, Sort.by(orderType, sortParam)));
        }

        return authors.stream()
                .map(toDtoMapper::mapAuthorToAuthorDto)
                .collect(Collectors.toList());
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

        long booksCount = bookRepository.getAllAuthorBooksOrdered(authorId, Sort.by(Sort.Direction.ASC)).size();

        if (booksCount > 0 && !forcibly) {
            throw new BooksInAuthorArePresentException(authorId, booksCount);
        }

        authorRepository.delete(toDelete);
    }
}