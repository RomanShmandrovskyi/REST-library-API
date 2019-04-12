package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.entity.exception.AuthorNotFoundException;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.repository.AuthorRepository;
import ua.com.epam.repository.JsonKeysConformity;
import ua.com.epam.repository.SqlQueryBuilder;
import ua.com.epam.service.mapper.ModelToDtoMapper;

import java.util.List;
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

    public AuthorGetDto findAuthorByAuthorId(long authorId) {
        Optional<Author> author = authorRepository.getAuthorByAuthorId(authorId);

        if (author.isPresent()) {
            return toDtoMapper.mapAuthorModelToDto_GET(author.get());
        }

        throw new AuthorNotFoundException(authorId);
    }

    public List<AuthorGetDto> getAllAuthorsSortedBy(String sortBy, String order) {
        Sort.Direction orderType;

        if (!JsonKeysConformity.ifJsonKeyExists(sortBy)) {
            throw new NoSuchJsonKeyException(sortBy);
        }

        if (order.equals("desc")) {
            orderType = Sort.Direction.DESC;
        } else {
            orderType = Sort.Direction.ASC;
        }

        String parameter = JsonKeysConformity.getPropNameByJsonKey(sortBy);

        return authorRepository.findAllOrderBy(Sort.by(orderType, parameter))
                .stream()
                .map(toDtoMapper::mapAuthorModelToDto_GET)
                .collect(Collectors.toList());
    }
}
