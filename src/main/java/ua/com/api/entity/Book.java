package ua.com.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import ua.com.api.service.util.annotation.ForSort;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"author", "genre"})
@NoArgsConstructor

@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", unique = true, nullable = false)
    private Long bookId;

    @ForSort(values = {"name", "bookName"})
    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "book_language", nullable = false, length = 50)
    private String bookLanguage;

    @Column(name = "book_description", length = 1000)
    private String bookDescription;

    @ForSort(values = {"pagesCount", "pageCount", "pagesNumber", "numberOfPages"})
    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "book_height")
    private Double bookHeight;

    @Column(name = "book_width")
    private Double bookWidth;

    @Column(name = "book_length")
    private Double bookLength;

    @ForSort(values = {"volume", "bookVolume"})
    @Formula(value = "book_height * book_width * book_length")
    private Double volume;

    @ForSort(values = {"square", "bookSquare"})
    @Formula(value = "book_width * book_length")
    private Double square;

    @ForSort(values = {"publicationYear", "publication", "year", "pubYear"})
    @Column(name = "publication_year")
    private Integer publicationYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genreId")
    private Genre genre;
}