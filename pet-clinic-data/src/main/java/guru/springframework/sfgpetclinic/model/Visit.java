package guru.springframework.sfgpetclinic.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Visit extends BaseEntity {

    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
