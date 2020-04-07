package guru.springframework.sfgpetclinic.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "owners")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Owner extends Person {

    @Column(name = "city")
    private String city;
    @Column(name = "address")
    private String address;
    @Column(name = "telefone")
    private String telephone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<Pet> pets = new HashSet<Pet>();

    public void addPet(Pet pet){
        this.pets.add(pet);
        pet.setOwner(this);
    }
}
