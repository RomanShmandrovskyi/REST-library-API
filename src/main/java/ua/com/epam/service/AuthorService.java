package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.entity.dto.book.AuthorBookDto;
import ua.com.epam.entity.dto.genre.AuthorGenreDto;
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

    public AuthorGetDto findAuthorByAuthorId(long authorId) {
        Optional<Author> exist = authorRepository.getOneByAuthorId(authorId);

        if (!exist.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author toGet = exist.get();
        return toDtoMapper.mapAuthorModelToGetDto(toGet);
    }

    public List<AuthorGenreDto> getAllGenresOfAuthor(long authorId) {
        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        return genreRepository.getAuthorGenres(authorId)
                .stream()
                .map(toDtoMapper::mapGenreToAuthorGenreDto)
                .collect(Collectors.toList());
    }

    public List<AuthorBookDto> getAllBooksOfAuthor(long authorId) {
        if (!authorRepository.existsByAuthorId(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }

        return bookRepository.getAuthorBooksByAuthorId(authorId)
                .stream()
                .map(toDtoMapper::mapBookToAuthorBookDto)
                .collect(Collectors.toList());
    }

    public List<AuthorGetDto> getAllAuthorsSortedBy(String sortBy, String order) {
        Sort.Direction orderType;

        if (order.equals("desc"))
            orderType = Sort.Direction.DESC;
        else
            orderType = Sort.Direction.ASC;

        String parameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        return authorRepository.findAllOrderBy(Sort.by(orderType, parameter))
                .stream()
                .map(toDtoMapper::mapAuthorModelToGetDto)
                .collect(Collectors.toList());
    }

    public List<AuthorGetDto> filterAuthors(Map<String, String> params) {
        String orderBy = JsonKeysConformity.getPropNameByJsonKey(params.remove("sortBy"));
        String orderType = params.remove("orderType");
        String limit = params.remove("limit");

        params.keySet().forEach(key -> {
            if (!JsonKeysConformity.ifJsonKeyExistsInGroup(key, JsonKeysConformity.Group.AUTHOR)) {
                throw new NoSuchJsonKeyException(key);
            }
        });

        Map<String, String> replaced = new HashMap<>();
        params.keySet().forEach(k -> replaced.put(JsonKeysConformity.getPropNameByJsonKey(k), params.get(k)));

        List<Author> filtered = queryBuilder.getFilteredEntities(replaced, orderBy, orderType, limit, Author.class);

        return filtered.stream()
                .map(toDtoMapper::mapAuthorModelToGetDto)
                .collect(Collectors.toList());
    }

    public AuthorGetDto addNewAuthor(AuthorDto author) {
        if (authorRepository.existsByAuthorId(author.getAuthorId())) {
            throw new AuthorAlreadyExistsException();
        }

        Author toPost = toModelMapper.mapAuthorDtoToAuthor(author);
        Author response = authorRepository.save(toPost);
        return toDtoMapper.mapAuthorModelToGetDto(response);
    }

    public AuthorGetDto updateExistedAuthor(long authorId, AuthorDto authorDto) {
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

        return toDtoMapper.mapAuthorModelToGetDto(updated);
    }

    public AuthorGetDto deleteExistedAuthor(long authorId, boolean forcibly) {
        Optional<Author> opt = authorRepository.getOneByAuthorId(authorId);

        if (!opt.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author toDelete = opt.get();
        long booksCount = bookRepository.getAuthorBooksByAuthorId(authorId).size();

        if (booksCount == 0) {
            authorRepository.delete(toDelete);
            return toDtoMapper.mapAuthorModelToGetDto(toDelete);
        }

        if (!forcibly) {
            throw new BooksInAuthorIsPresentException(authorId, booksCount);
        }

        authorRepository.delete(toDelete);
        return toDtoMapper.mapAuthorModelToGetDto(toDelete);
    }
}
