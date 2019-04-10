package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.epam.assembler.mapper.ModelToDtoMapper;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ModelToDtoMapper toDtoMapper;

    public AuthorGetDto findAuthorByAuthorId(long authorId) {
        Author author = authorRepository.getAuthorByAuthorId(authorId).get();
        return toDtoMapper.getAuthorGetDtoFromModel(author);
    }

    public List<AuthorGetDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(toDtoMapper::getAuthorGetDtoFromModel)
                .collect(Collectors.toList());
    }
}
