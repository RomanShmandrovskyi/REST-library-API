package ua.com.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import ua.com.api.service.util.annotation.ForSort;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"author", "genre"})
@NoArgsConstructor

@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", unique = true)
    @ForSort(defaultValue = "bookId",
            aliases = {"id"})
    private Long bookId;

    @Column(name = "book_name", nullable = false)
    @ForSort(defaultValue = "bookName",
            aliases = {"name", "nameOfBook"})
    private String bookName;

    @Column(name = "book_language", nullable = false, length = 50)
    private String bookLanguage;

    @Column(name = "book_description", length = 1000)
    private String bookDescription;

    @Column(name = "page_count")
    @ForSort(defaultValue = "pagesCount",
            aliases = {"pageCount", "countOfPages", "pagesNumber", "numberOfPages"})
    private Integer pagesCount;

    @Column(name = "book_height")
    private Double bookHeight;

    @Column(name = "book_width")
    private Double bookWidth;

    @Column(name = "book_length")
    private Double bookLength;

    @Formula(value = "book_height * book_width * book_length")
    @ForSort(defaultValue = "volume",
            aliases = {"bookVolume", "volumeOfBook"})
    private Double volume;

    @Formula(value = "book_width * book_length")
    @ForSort(defaultValue = "square",
            aliases = {"squareOfBook", "bookSquare"})
    private Double square;

    @Column(name = "publication_year")
    @ForSort(defaultValue = "publicationYear",
            aliases = {"publication", "year", "pubYear"})
    private Integer publicationYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genreId")
    private Genre genre;
}