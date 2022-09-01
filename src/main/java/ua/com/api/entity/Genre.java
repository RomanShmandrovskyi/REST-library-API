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
    @Column(name = "genre_id", unique = true, nullable = false)
    @ForSort(defaultValue = "id",
            aliases = {"genreId"})
    private Long genreId;

    @Column(name = "genre_name", unique = true, nullable = false, length = 50)
    @ForSort(defaultValue = "name",
            aliases = {"genreName", "nameOfGenre"})
    private String genreName;

    @Column(name = "genre_description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "genre", orphanRemoval = true)
    private List<Book> books;
}
