package ua.com.epam.repository;

import java.util.stream.Stream;

public enum JsonKeysConformity {
    AUTHOR_FIRST_NAME("authorName.first", "firstName"),
    AUTHOR_SECOND_NAME("authorName.second", "secondName"),
    AUTHOR_BIRTH_DATE("birth.date", "birthDate"),
    AUTHOR_BIRTH_COUNTRY("birth.country", "birthCountry"),
    AUTHOR_BIRTH_CITY("birth.city", "birthCity"),
    AUTHOR_ID("authorId", "authorId"),
    AUTHOR_DESCRIPTION("description", "description"),
    AUTHOR_NATIONALITY("nationality", "nationality"),
    AUTHOR_BOOKS_COUNT("booksCount", "booksCount"),

    GENRE_ID("genreId", "genreId"),
    GENRE_NAME("genreName", "genreName"),
    GENRE_DESCRIPTION("genreDescription", "genreDescription"),
    GENRE_BOOKS_COUNT("booksCount", "booksCount"),

    BOOK_ID("bookId", "bookId"),
    BOOK_NAME("bookName", "bookName"),
    BOOK_LANGUAGE("bookLang", "bookLang"),
    BOOK_DESCRIPTION("bookDescription", "bookDescription"),
    BOOK_PAGE_COUNT("additional.pageCount", "pageCount"),
    BOOK_HEIGHT("additional.size.height", "bookHeight"),
    BOOK_WIDTH("additional.size.width", "bookWidth"),
    BOOK_LENGTH("additional.size.length", "bookLength"),
    BOOK_PUBLICATION_YEAR("publicationYear", "publicationYear");

    private final String jsonPropertyKey;
    private final String modelPropertyName;

    JsonKeysConformity(String jsonPropertyKey, String modelPropertyName) {
        this.jsonPropertyKey = jsonPropertyKey;
        this.modelPropertyName = modelPropertyName;
    }

    public static String getPropNameByJsonKey(String jsonKey) {
        return Stream.of(JsonKeysConformity.values())
                .filter(k -> k.jsonPropertyKey.equals(jsonKey))
                .findFirst()
                .get()
                .modelPropertyName;
    }

    public static boolean ifJsonKeyExists(String jsonKey) {
        return Stream.of(JsonKeysConformity.values())
                .anyMatch(k -> k.jsonPropertyKey.equals(jsonKey));
    }
}