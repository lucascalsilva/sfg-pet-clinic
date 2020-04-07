package guru.springframework.sfgpetclinic.model;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "types")
@NoArgsConstructor
@SuperBuilder
public class PetType extends NamedEntity {

}
