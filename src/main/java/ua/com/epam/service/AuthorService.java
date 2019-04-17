package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.entity.exception.AuthorAlreadyExistsException;
import ua.com.epam.entity.exception.AuthorNotFoundException;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.entity.exception.type.IdMismatchException;
import ua.com.epam.repository.AuthorRepository;
import ua.com.epam.repository.JsonKeysConformity;
import ua.com.epam.repository.SqlQueryBuilder;
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
    private SqlQueryBuilder queryBuilder;

    @Autowired
    private ModelToDtoMapper toDtoMapper;

    @Autowired
    private DtoToModelMapper toModelMapper;

    public AuthorGetDto findAuthorByAuthorId(long authorId) {
        Optional<Author> exist = authorRepository.getAuthorByAuthorId(authorId);

        if (!exist.isPresent()) {
            throw new AuthorNotFoundException(authorId);
        }

        Author toGet = exist.get();
        return toDtoMapper.mapAuthorModelToGetDto(toGet);
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

/*    public AuthorGetDto deleteExistedAuthorWithBooks(long authorId, boolean force) {

    }*/
}
