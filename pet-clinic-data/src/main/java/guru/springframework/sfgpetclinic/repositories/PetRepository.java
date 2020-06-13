package guru.springframework.sfgpetclinic.repositories;

import guru.springframework.sfgpetclinic.model.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PetRepository extends CrudRepository<Pet, Long> {

    Optional<Pet> findByNameAndBirthDate(String name, LocalDate birthDate);
}
