package ua.com.epam.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "genre_id", unique = true, nullable = false)
    private long genreId;

    @Column(name = "genre_name", length = 50, nullable = false)
    private String genreName;

    @Column(name = "genre_descr" , length = 1000)
    private String genreDescription;

    @Column(name = "books_count")
    private int booksCount;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private List<Book> books;
}
