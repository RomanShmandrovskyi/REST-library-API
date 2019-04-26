package ua.com.epam.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GroupByBooksCount {
    @Id
    private Long id;
    private String name;
    private Long booksCount;
}
