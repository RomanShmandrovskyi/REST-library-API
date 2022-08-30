package ua.com.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.api.service.util.annotation.ForSort;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"books"})
@NoArgsConstructor

@Entity
@Table(name = "genre")
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genre_id", unique = true, nullable = false)
    private Long genreId;

    @ForSort(values = {"name", "genreName", "nameOfGenre"})
    @Column(name = "genre_name", length = 50, unique = true, nullable = false)
    private String genreName;

    @Column(name = "genre_description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "genre", orphanRemoval = true)
    private List<Book> books;
}
