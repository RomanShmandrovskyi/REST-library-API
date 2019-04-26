package ua.com.epam;

import com.github.javafaker.Faker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class DataIngestion {
    public static void main(String[] args) throws ParseException {
        Faker f = new Faker();
        String doPost = "curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d \"%s\" '%s'";

        String authorPostEnd = "localhost:8080/api/library/author/new";
        String genrePostEnd = "localhost:8080/api/library/genre/new";
        String bookPostEnd = "localhost:8080/api/library/book/%s/%s/new";

        //Author
        //generate unique Author ids;
        List<Long> authorIds = new ArrayList<>();
        while (authorIds.size() < 150) {
            long id = f.number().numberBetween(1L, 9999L);
            if (!authorIds.contains(id)) authorIds.add(id);
        }

        String[] nationalitites = {"Albanian", "American", "Australian", "Austrian", "Belgian",
                "British", "Bulgarian", "Canadian", "Chinese", "Czech", "Dutch", "Egyptian",
                "French", "German", "Greek", "Indian", "Irish", "Lithuanian", "Malaysian",
                "Mexican", "Moldovan", "New Zealander", "Romanian", "Scottish", "Spanish",
                "Swedish", "Turkish", "Ukrainian", "Welsh", "Syrian", "Slovenian", "Slovakian",
                "Polish", "Peruvian", "Namibian", "Nepalese", "Afghan", "Andorran", "Angolan",
                "Armenian", "Bahamian", "Cambodian", "Central African", "Colombian", "Cuban",
                "Equatorial Guinean", "Icelander", "Indonesian", "Kittian and Nevisian",
                "Liechtensteiner", "Lithuanian", "Luxembourger", "Maldivan", "Mongolian"};

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date from = formatter.parse("1920-01-02");
        Date to = formatter.parse("2000-12-31");

        authorIds.stream()
                .map(id -> getAuthorJSON(
                        id,
                        f.name().firstName(), f.name().lastName(),
                        nationalitites[new Random().nextInt(nationalitites.length)],
                        formatter.format(f.date().between(from, to)), f.address().country(), f.address().city(),
                        f.lorem().paragraph()))
                .forEach(o -> System.out.println(String.format(doPost, o, authorPostEnd)));

        //genre
        //generate all possible genre names
        List<String> genreNames = new ArrayList<>();
        while (genreNames.size() < 30) {
            String name = f.book().genre();
            if (!genreNames.contains(name)) genreNames.add(name);
        }

        //generate unique genre ids
        List<Long> genreIds = new ArrayList<>();
        while (genreIds.size() < genreNames.size()) {
            long id = f.number().numberBetween(1, 9999);
            if (!genreIds.contains(id)) genreIds.add(id);
        }

        IntStream.range(0, genreIds.size())
                .mapToObj(i -> getGenreJSON(genreIds.get(i), genreNames.get(i), f.lorem().paragraph()))
                .forEach(o -> System.out.println(String.format(doPost, o, genrePostEnd)));

        //book
        List<Long> bookIds = new ArrayList<>();
        while (bookIds.size() < 1200) {
            long id = f.number().numberBetween(1, 9999);
            if (!bookIds.contains(id)) bookIds.add(id);
        }

        String[] languages = {"ukrainian", "german", "russian", "polish", "spanish", "belorussian", "chinese", "english"};

        bookIds.stream()
                .map(bookId -> getBookJSON(bookId,
                        f.book().title(),
                        languages[new Random().nextInt(languages.length - 1)],
                        f.lorem().paragraph(),
                        f.number().numberBetween(10, 1000),
                        f.number().randomDouble(1, 5, 40),
                        f.number().randomDouble(1, 5, 40),
                        f.number().randomDouble(1, 5, 40),
                        f.number().numberBetween(1970, 2019)))
                .forEach(o -> System.out.println(
                        String.format(doPost, o, String.format(
                                bookPostEnd,
                                authorIds.get(new Random().nextInt(authorIds.size() - 1)),
                                genreIds.get(new Random().nextInt(genreIds.size() - 1))))));
    }

    private static String getAuthorJSON(long authorId, String first, String second,
                                        String nationality, String birthDate, String country, String city, String descr) {
        return "{" +
                "\\\"authorId\\\":" + authorId + "," +
                "\\\"authorName\\\":{" +
                "\\\"first\\\":\\\"" + first + "\\\"," +
                "\\\"second\\\":\\\"" + second + "\\\"" +
                "}," +
                "\\\"nationality\\\":\\\"" + nationality + "\\\"," +
                "\\\"birth\\\":{" +
                "\\\"date\\\":\\\"" + birthDate + "\\\"," +
                "\\\"country\\\":\\\"" + country + "\\\"," +
                "\\\"city\\\":\\\"" + city + "\\\"" +
                "}," +
                "\\\"description\\\":\\\"" + descr + "\\\"" +
                "}";
    }

    private static String getGenreJSON(long genreId, String genreName, String genreDescription) {
        return "{" +
                "\\\"genreId\\\":" + genreId + "," +
                "\\\"genreName\\\":\\\"" + genreName + "\\\"," +
                "\\\"description\\\":\\\"" + genreDescription + "\\\"" +
                "}";
    }

    private static String getBookJSON(long bookId, String bookName, String bookLang, String bookDescr, int pageCount, double height, double width, double length, int pubYear) {
        return "{" +
                "\\\"bookId\\\":" + bookId + "," +
                "\\\"bookName\\\":\\\"" + bookName + "\\\"," +
                "\\\"bookLanguage\\\":\\\"" + bookLang + "\\\"," +
                "\\\"description\\\":\\\"" + bookDescr + "\\\"," +
                "\\\"additional\\\":{\\\"pageCount\\\":" + pageCount + "," +
                "\\\"size\\\":{\\\"height\\\":" + height + "," +
                "\\\"width\\\":" + width + "," +
                "\\\"length\\\":" + length + "}}," +
                "\\\"publicationYear\\\":" + pubYear + "}";
    }
}
