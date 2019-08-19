package ua.com.epam.repository;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum JsonKeysConformity {
    AUTHOR_FIRST_NAME("authorName.first", "firstName"),
    AUTHOR_SECOND_NAME("authorName.second", "secondName"),
    AUTHOR_BIRTH_DATE("birth.date", "birthDate"),
    AUTHOR_BIRTH_COUNTRY("birth.country", "birthCountry"),
    AUTHOR_BIRTH_CITY("birth.city", "birthCity"),
    AUTHOR_ID("authorId", "authorId"),
    AUTHOR_DESCRIPTION("authorDescription", "description"),
    AUTHOR_NATIONALITY("nationality", "nationality"),

    GENRE_ID("genreId", "genreId"),
    GENRE_NAME("genreName", "genreName"),
    GENRE_DESCRIPTION("genreDescription", "description"),

    BOOK_ID("bookId", "bookId"),
    BOOK_NAME("bookName", "bookName"),
    BOOK_LANGUAGE("bookLanguage", "bookLang"),
    BOOK_DESCRIPTION("bookDescription", "description"),
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

    public static boolean ifJsonKeyExistsInGroup(String jsonKey, JsonKeysConformity.Group group) {
        return JsonKeysConformity.Group.getGroup(group)
                .stream()
                .anyMatch(k -> k.jsonPropertyKey.equals(jsonKey));
    }

    public enum Group {
        AUTHOR("author"),
        GENRE("genre"),
        BOOK("book");

        private final String value;

        Group(String value) {
            this.value = value;
        }

        private static String getGroupByName(String name) {
            return Stream.of(Group.values())
                    .filter(g -> g.value.equals(name))
                    .findFirst()
                    .get()
                    .toString();
        }

        private static List<JsonKeysConformity> getGroup(JsonKeysConformity.Group group) {
            return Stream.of(JsonKeysConformity.values())
                    .filter(jc -> jc.toString().startsWith(Group.getGroupByName(group.value)))
                    .collect(Collectors.toList());
        }
    }
}