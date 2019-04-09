package ua.com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.epam.entity.Author;
import ua.com.epam.repository.AuthorRepository;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author findAuthorByAuthorId(long authorId) {
        return authorRepository.getAuthorByAuthorId(authorId).orElse(new Author());
    }
}
