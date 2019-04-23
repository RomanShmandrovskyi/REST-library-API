package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.SimpleAuthorWithBooksDto;
import ua.com.epam.entity.dto.author.SimpleAuthorWithGenresDto;
import ua.com.epam.entity.exception.IdMismatchException;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.entity.exception.author.AuthorAlreadyExistsException;
import ua.com.epam.entity.exception.author.AuthorNotFoundException;
import ua.com.epam.entity.exception.author.BooksInAuthorIsPresentException;
import ua.com.epam.repository.*;
import ua.com.epam.service.mapper.DtoToModelMapper;
import ua.com.epam.service.mapper.ModelToDtoMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private SqlQueryBuilder queryBuilder;

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

    public SimpleAuthorWithGenresDto findAuthorWithAllItGenres(long authorId) {
        Optional<Author> opt = authorRepository.getOneByAuthorId(authorId);

        if (!opt.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author author = opt.get();
        List<Genre> authorGenres = genreRepository.getAllGenresOfAuthor(authorId);

        return toDtoMapper.getSimpleAuthorWithGenresDto(author, authorGenres);
    }

    public SimpleAuthorWithBooksDto findAuthorWithAllItBooks(long authorId) {
        Optional<Author> opt = authorRepository.getOneByAuthorId(authorId);

        if (!opt.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author author = opt.get();
        List<Book> authorBooks = bookRepository.getAllAuthorBooks(authorId);

        return toDtoMapper.getSimpleAuthorWithBooksDto(author, authorBooks);
    }

    public List<AuthorDto> findAllAuthorsSortedPaginated(String sortBy, String order, int page, int size) {
        Sort.Direction orderType = getSortDirection(order);
        String parameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        return authorRepository.getAllAuthorsOrdered(Sort.by(orderType, parameter))
                .stream()
                .skip((page - 1) * size)
                .limit(size)
                .map(toDtoMapper::mapAuthorToAuthorDto)
                .collect(Collectors.toList());
    }

    public List<AuthorDto> findFilteredAuthors(Map<String, String> params) {
        String orderBy = JsonKeysConformity.getPropNameByJsonKey(params.remove("sortBy"));
        String orderType = params.remove("orderType");

        params.keySet().forEach(key -> {
            if (!JsonKeysConformity.ifJsonKeyExistsInGroup(key, JsonKeysConformity.Group.AUTHOR)) {
                throw new NoSuchJsonKeyException(key);
            }
        });

        Map<String, String> replaced = new HashMap<>();
        params.keySet().forEach(k -> replaced.put(JsonKeysConformity.getPropNameByJsonKey(k), params.get(k)));

        List<Author> filtered = queryBuilder.getFilteredEntities(replaced, orderBy, orderType, Author.class);

        return filtered.stream()
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
        proxy.setDescription(authorDto.getDescription());
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
            throw new BooksInAuthorIsPresentException(authorId, booksCount);
        }

        authorRepository.delete(toDelete);
        return toDtoMapper.mapAuthorToAuthorDto(toDelete);
    }
}
