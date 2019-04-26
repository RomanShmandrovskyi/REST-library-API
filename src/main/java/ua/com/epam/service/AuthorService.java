package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.GroupByBooksCount;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.AuthorGroupByBooksDto;
import ua.com.epam.entity.exception.IdMismatchException;
import ua.com.epam.entity.exception.author.AuthorAlreadyExistsException;
import ua.com.epam.entity.exception.author.AuthorNotFoundException;
import ua.com.epam.entity.exception.author.BooksInAuthorArePresentException;
import ua.com.epam.entity.exception.book.BookNotFoundException;
import ua.com.epam.entity.exception.genre.GenreNotFoundException;
import ua.com.epam.repository.*;
import ua.com.epam.service.mapper.DtoToModelMapper;
import ua.com.epam.service.mapper.ModelToDtoMapper;

import java.util.*;
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
    private GroupByBooksRepository group;

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
        Optional<Author> exist = authorRepository.getOneByAuthorId(authorId);

        if (!exist.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author toGet = exist.get();
        return toDtoMapper.mapAuthorToAuthorDto(toGet);
    }

    public AuthorDto findAuthorOfBook(long bookId) {
        if (!bookRepository.existsByBookId(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        Author toGet =authorRepository.getAuthorOfBook(bookId);
        return toDtoMapper.mapAuthorToAuthorDto(toGet);
    }

    public List<AuthorDto> findAllAuthors(String sortBy, String order, int page, int size, boolean pagination) {
        Sort.Direction orderType = getSortDirection(order);
        String sortParam = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        if (pagination) {
            return authorRepository
                    .getAllAuthorsOrderedPaginated(Sort.by(orderType, sortParam), PageRequest.of(page - 1, size))
                    .stream()
                    .map(toDtoMapper::mapAuthorToAuthorDto)
                    .collect(Collectors.toList());
        }

        return authorRepository
                .getAllAuthorsOrdered(Sort.by(orderType, sortParam))
                .stream()
                .map(toDtoMapper::mapAuthorToAuthorDto)
                .collect(Collectors.toList());
    }

    public List<AuthorDto> findAllAuthorsOfGenre(long genreId, String sortBy, String order, int page, int size, boolean pageable) {
        if (!genreRepository.existsByGenreId(genreId)) {
            throw new GenreNotFoundException(genreId);
        }

        Sort.Direction orderType = getSortDirection(order);
        String sortParameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        if (pageable) {
            return authorRepository
                    .getAllAuthorsInGenreOrderedPaginated(genreId, Sort.by(orderType, sortParameter), PageRequest.of(page - 1, size))
                    .stream()
                    .map(toDtoMapper::mapAuthorToAuthorDto)
                    .collect(Collectors.toList());
        }

        return authorRepository
                .getAllAuthorsInGenreOrdered(genreId, Sort.by(orderType, sortParameter))
                .stream()
                .map(toDtoMapper::mapAuthorToAuthorDto)
                .collect(Collectors.toList());
    }

    public AuthorGroupByBooksDto findAuthorWithBooksCount(long authorId) {
        Optional<GroupByBooksCount> opt = group.getAuthorGroupByBooks(authorId);

        if (!opt.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        GroupByBooksCount group = opt.get();

        return toDtoMapper.mapGroupModelToAuthorGroup(group);
    }

    public List<AuthorGroupByBooksDto> findAllAuthorsWithBooksCount(int page, int size, boolean pagination) {
        if (pagination) {
            return group.getAllAuthorsGroupByBooksPaginated(PageRequest.of(page - 1, size))
                    .stream()
                    .map(toDtoMapper::mapGroupModelToAuthorGroup)
                    .collect(Collectors.toList());
        }

        return group.getAllAuthorsGroupByBooks()
                .stream()
                .map(toDtoMapper::mapGroupModelToAuthorGroup)
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

    public AuthorDto deleteExistedAuthor(long authorId, boolean forcibly) {
        Optional<Author> opt = authorRepository.getOneByAuthorId(authorId);

        if (!opt.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author toDelete = opt.get();
        long booksCount = bookRepository.getAllAuthorBooks(authorId).size();

        if (booksCount > 0 && !forcibly) {
            throw new BooksInAuthorArePresentException(authorId, booksCount);
        }

        authorRepository.delete(toDelete);
        return toDtoMapper.mapAuthorToAuthorDto(toDelete);
    }
}
