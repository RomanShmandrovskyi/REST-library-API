package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.SimpleAuthorWithBooksDto;
import ua.com.epam.entity.dto.author.SimpleAuthorWithGenresDto;
import ua.com.epam.entity.dto.author.nested.NameDto;
import ua.com.epam.entity.dto.book.SimpleBookDto;
import ua.com.epam.entity.dto.genre.SimpleGenreDto;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.entity.exception.author.AuthorAlreadyExistsException;
import ua.com.epam.entity.exception.author.AuthorNotFoundException;
import ua.com.epam.entity.exception.author.BooksInAuthorIsPresentException;
import ua.com.epam.entity.exception.type.IdMismatchException;
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

    public AuthorDto findAuthorByAuthorId(long authorId) {
        Optional<Author> exist = authorRepository.getOneByAuthorId(authorId);

        if (!exist.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author toGet = exist.get();
        return toDtoMapper.mapAuthorToAuthorDto(toGet);
    }

    public SimpleAuthorWithGenresDto getAuthorWithAllItGenres(long authorId) {
        Optional<Author> opt = authorRepository.getOneByAuthorId(authorId);

        if (!opt.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author a = opt.get();

        List<SimpleGenreDto> authorGenres = genreRepository.getAllGenresOfAuthorByAuthorId(authorId)
                .stream()
                .map(toDtoMapper::mapGenreToSimpleGenreDto)
                .collect(Collectors.toList());

        return new SimpleAuthorWithGenresDto(
                a.getAuthorId(),
                new NameDto(a.getFirstName(), a.getSecondName()),
                authorGenres);
    }

    public SimpleAuthorWithBooksDto getAuthorWithAllItBooks(long authorId) {
        Optional<Author> opt = authorRepository.getOneByAuthorId(authorId);

        if (!opt.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author a = opt.get();

        List<SimpleBookDto> authorBooks = bookRepository.getAuthorBooksByAuthorId(authorId)
                .stream()
                .map(toDtoMapper::mapBookToSimpleBookDto)
                .collect(Collectors.toList());

        return new SimpleAuthorWithBooksDto(
                a.getAuthorId(),
                new NameDto(a.getFirstName(), a.getSecondName()),
                authorBooks);
    }

    public List<AuthorDto> getAllAuthorsSortedBy(String sortBy, String order, int page, int size) {
        Sort.Direction orderType;

        if (order.equals("desc"))
            orderType = Sort.Direction.DESC;
        else
            orderType = Sort.Direction.ASC;

        String parameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        return authorRepository.findAllOrderBy(Sort.by(orderType, parameter))
                .stream()
                .map(toDtoMapper::mapAuthorToAuthorDto)
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<AuthorDto> filterAuthors(Map<String, String> params) {
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

        if (!opt.isPresent()) throw new AuthorNotFoundException(authorId);
        if (authorId != authorDto.getAuthorId()) throw new IdMismatchException();

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
        long booksCount = bookRepository.getAuthorBooksByAuthorId(authorId).size();

        if (booksCount == 0) {
            authorRepository.delete(toDelete);
            return toDtoMapper.mapAuthorToAuthorDto(toDelete);
        }

        if (!forcibly) {
            throw new BooksInAuthorIsPresentException(authorId, booksCount);
        }

        authorRepository.delete(toDelete);
        return toDtoMapper.mapAuthorToAuthorDto(toDelete);
    }
}
