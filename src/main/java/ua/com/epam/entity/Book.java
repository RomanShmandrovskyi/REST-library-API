package ua.com.epam.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", unique = true, nullable = false)
    private Long bookId;

    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "book_language", nullable = false, length = 50)
    private String bookLang;

    @Column(name = "book_descr", length = 1000)
    private String description;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "book_height")
    private Double bookHeight;

    @Column(name = "book_width")
    private Double bookWidth;

    @Column(name = "book_length")
    private Double bookLength;

    @Formula(value = "book_height * book_width * book_length")
    private Double volume;

    @Formula(value = "book_width * book_length")
    private Double square;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "genre_id")
    private Long genreId;
}