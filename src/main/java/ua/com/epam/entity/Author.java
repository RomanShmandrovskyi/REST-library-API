package ua.com.epam.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "author_id", unique = true, nullable = false)
    private long authorId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "second_name", nullable = false, length = 50)
    private String secondName;

    @Column(name = "author_descr", length = 1000)
    private String description;

    @Column(length = 30)
    private String nationality;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "birth_country", length = 50)
    private String birthCountry;

    @Column(name = "birth_city", length = 50)
    private String birthCity;

    @Column(name = "books_count")
    private int booksCount;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books;
}
