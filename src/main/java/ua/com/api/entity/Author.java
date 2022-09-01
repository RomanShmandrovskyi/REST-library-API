package ua.com.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.api.service.util.annotation.ForSort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"books"})
@NoArgsConstructor

@Entity
@Table(name = "author")
public class Author implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", unique = true)
    @ForSort(defaultValue = "id",
            aliases = {"authorId"})
    private Long authorId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "full_name", unique = true)
    @ForSort(defaultValue = "name",
            aliases = {"authorName", "fullName", "authorFullName"})
    private String fullName;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "nationality", length = 30)
    private String nationality;

    @Column(name = "birth_date")
    @ForSort(defaultValue = "birthDate",
            aliases = {"birth", "authorBirthDate", "authorBirth"})
    private LocalDate birthDate;

    @Column(name = "birth_country", length = 60)
    private String birthCountry;

    @Column(name = "birth_city", length = 50)
    private String birthCity;

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<Book> books;
}
