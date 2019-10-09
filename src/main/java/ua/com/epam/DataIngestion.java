package ua.com.epam;

import com.github.javafaker.Faker;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataIngestion {
    private final static String fileName = "data.sh";
    private final static String fileLocation = "src/main/resources";

    private final static int authorsCount = 150; // 9999 - is maximum (if set to max it greatly increase generation time)
    private final static int genresCount = 30;   // 30 is maximum; if set more, will work endlessly!!!
    private final static int booksCount = 1000;  // 9999 - is maximum (if set to max it greatly increase generation time)

    private static Faker f = new Faker();

    public static void main(String[] args) throws ParseException {
        List<String> bashLines = new ArrayList<>();
        bashLines.add("#!/usr/bin/env bash");
        String doPost = "curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d \"%s\" '%s'";

        String authorPostEnd = "localhost:8080/api/library/author";
        String genrePostEnd = "localhost:8080/api/library/genre";
        String bookPostEnd = "localhost:8080/api/library/book/%s/%s";

        //Author
        //generate unique Author ids;
        List<Long> authorIds = new ArrayList<>();
        while (authorIds.size() < authorsCount) {
            long id = f.number().numberBetween(1L, 9999L);
            if (!authorIds.contains(id)) authorIds.add(id);
        }

        String[] nationalities = {"Albanian", "American", "Australian", "Austrian", "Belgian", "British", "Bulgarian",
                "Canadian", "Chinese", "Czech", "Dutch", "Egyptian", "French", "German", "Greek", "Indian", "Irish",
                "Lithuanian", "Malaysian", "Mexican", "Moldovan", "New Zealander", "Romanian", "Scottish", "Spanish",
                "Swedish", "Turkish", "Ukrainian", "Welsh", "Syrian", "Slovenian", "Slovakian", "Polish", "Peruvian",
                "Namibian", "Nepalese", "Afghan", "Andorran", "Angolan", "Armenian", "Bahamian", "Cambodian",
                "Central African", "Colombian", "Cuban", "Equatorial Guinean", "Icelander", "Indonesian",
                "Kittian and Nevisian", "Liechtensteiner", "Lithuanian", "Luxembourger", "Maldivan", "Mongolian"};

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date from = formatter.parse("1920-01-01");
        Date to = formatter.parse("1999-12-31");

        authorIds.stream()
                .map(id -> getAuthorJSON(
                        id,
                        f.name().firstName(), f.name().lastName(),
                        nationalities[new Random().nextInt(nationalities.length)],
                        formatter.format(f.date().between(from, to)), f.address().country(), f.address().city(),
                        f.lorem().paragraph()))
                .forEach(o -> bashLines.add(String.format(doPost, o, authorPostEnd)));

        //genre
        //generate all possible genre names
        List<String> genreNames = new ArrayList<>();
        while (genreNames.size() < genresCount) {
            String name = f.book().genre();
            if (!genreNames.contains(name)) genreNames.add(name);
        }

        //generate unique genre ids
        List<Long> genreIds = new ArrayList<>();
        while (genreIds.size() < genreNames.size()) {
            long id = f.number().numberBetween(1L, 9999L);
            if (!genreIds.contains(id)) genreIds.add(id);
        }

        IntStream.range(0, genreIds.size())
                .mapToObj(i -> getGenreJSON(genreIds.get(i), genreNames.get(i), f.lorem().paragraph()))
                .forEach(o -> bashLines.add(String.format(doPost, o, genrePostEnd)));

        //book
        List<Long> bookIds = new ArrayList<>();
        while (bookIds.size() < booksCount) {
            long id = f.number().numberBetween(1L, 9999L);
            if (!bookIds.contains(id)) bookIds.add(id);
        }

        String[] languages = {"ukrainian", "german", "russian", "polish", "spanish", "belorussian", "chinese", "english",
                "portuguese", "croatian", "french", "arabic", "armenian", "urdu", "farsi"};

        bookIds.stream()
                .map(bookId -> getBookJSON(bookId,
                        generateBookName(),
                        languages[new Random().nextInt(languages.length)],
                        f.lorem().paragraph(),
                        f.number().numberBetween(10, 1000),
                        f.number().randomDouble(1, 5, 40),
                        f.number().randomDouble(1, 1, 5),
                        f.number().randomDouble(1, 5, 40),
                        f.number().numberBetween(1970, 2019)))
                .forEach(o -> bashLines.add(String.format(doPost, o, String.format(
                        bookPostEnd,
                        authorIds.get(new Random().nextInt(authorIds.size())),
                        genreIds.get(new Random().nextInt(genreIds.size()))))));

        File script = new File(fileLocation + "/" + fileName);
        try {
            script.createNewFile();
            Files.write(script.toPath(), bashLines);
            System.out.println("File " + fileName + " created in: " + fileLocation + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateBookName() {
        List<String> words = new ArrayList<>();
        IntStream.rangeClosed(0, f.number().numberBetween(2, 5))
                .forEach(i -> words.add(f.lorem().word()));
        Collections.shuffle(words);
        String first = words.get(0);
        words.set(0, first.substring(0, 1).toUpperCase() + first.substring(1));
        return words.stream().collect(Collectors.joining(" "));
    }

    private static String getAuthorJSON(long authorId, String first, String second, String nationality,
                                        String birthDate, String country, String city, String descr) {
        return new JSONObject()
                .put("authorId", authorId)
                .put("authorName", new JSONObject()
                        .put("first", first)
                        .put("second", second))
                .put("nationality", nationality)
                .put("birth", new JSONObject()
                        .put("date", birthDate)
                        .put("country", country)
                        .put("city", city))
                .put("authorDescription", descr)

                .toString()
                .replace("\"", "\\\"");
    }

    private static String getGenreJSON(long genreId, String genreName, String genreDescription) {
        return new JSONObject()
                .put("genreId", genreId)
                .put("genreName", genreName)
                .put("genreDescription", genreDescription)

                .toString()
                .replace("\"", "\\\"");
    }

    private static String getBookJSON(long bookId, String bookName, String bookLang, String bookDescr, int pageCount,
                                      double height, double width, double length, int pubYear) {
        return new JSONObject()
                .put("bookId", bookId)
                .put("bookName", bookName)
                .put("bookLanguage", bookLang)
                .put("bookDescription", bookDescr)
                .put("additional", new JSONObject()
                        .put("pageCount", pageCount)
                        .put("size", new JSONObject()
                                .put("height", height)
                                .put("width", width)
                                .put("length", length)))
                .put("publicationYear", pubYear)

                .toString()
                .replace("\"", "\\\"");
    }
}
