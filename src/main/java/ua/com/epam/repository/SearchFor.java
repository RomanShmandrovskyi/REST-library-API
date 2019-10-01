package ua.com.epam.repository;

import org.springframework.stereotype.Repository;
import ua.com.epam.entity.Author;
import ua.com.epam.entity.Book;
import ua.com.epam.entity.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SearchFor {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Author> authors(String query) {
        List<String> columns = new ArrayList<>(Arrays.asList("firstName", "secondName"));
        List<String> words = Arrays.asList(query.split(" "));

        return search(columns, words, Author.class);
    }

    public List<Genre> genres(String query) {
        List<String> columns = new ArrayList<>(Collections.singletonList("genreName"));
        List<String> words = Arrays.asList(query.split(" "));

        return search(columns, words, Genre.class);
    }

    public List<Book> books(String query) {
        List<String> columns = new ArrayList<>(Collections.singletonList("bookName"));
        List<String> words = Arrays.asList(query.split(" "));

        return search(columns, words, Book.class);
    }

    private String buildQuery(List<String> columnNames, List<String> valuesToSearch) {
        String or = " OR ";
        String like = " LIKE ";
        String value = "\'%%%s%%\'";

        return columnNames.stream()
                .map(c -> valuesToSearch.stream()
                        .map(v -> "t." + c + like + String.format(value, v))
                        .collect(Collectors.joining(or)))
                .collect(Collectors.joining(or));
    }

    private List search(List<String> columnNames, List<String> valuesToSearch, Class clazz) {
        String prefix = "SELECT t FROM " + clazz.getName() + " t WHERE ";

        List<String> valsMods = valuesToSearch.stream().filter(v -> !v.equals("")).collect(Collectors.toList());
        if (valsMods.size() == 0) {
            return new ArrayList<>();
        }

        String queryToExecute = prefix + buildQuery(columnNames, valuesToSearch);

        return entityManager.createQuery(queryToExecute).getResultList();
    }
}
