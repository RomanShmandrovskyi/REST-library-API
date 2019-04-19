package ua.com.epam.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private long id;

    @Column(name = "book_id", unique = true, nullable = false)
    private long bookId;

    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "book_language", nullable = false, length = 50)
    private String bookLang;

    @Column(name = "book_descr", length = 1000)
    private String bookDescription;

    @Column(name = "page_count")
    private int pageCount;

    @Column(name = "book_height")
    private double bookHeight;

    @Column(name = "book_width")
    private double bookWidth;

    @Column(name = "book_length")
    private double bookLength;

    @Column(name = "publication_year")
    private int publicationYear;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "genre_id")
    private Long genreId;
}
