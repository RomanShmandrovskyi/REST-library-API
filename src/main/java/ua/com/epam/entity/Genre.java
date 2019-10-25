package ua.com.epam.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genre_id", unique = true, nullable = false)
    private Long genreId;

    @Column(name = "genre_name", length = 50, unique = true, nullable = false)
    private String genreName;

    @Column(name = "genre_descr" , length = 1000)
    private String description;
}
