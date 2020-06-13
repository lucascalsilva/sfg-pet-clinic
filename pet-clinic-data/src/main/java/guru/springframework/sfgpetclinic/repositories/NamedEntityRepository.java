package guru.springframework.sfgpetclinic.repositories;

import guru.springframework.sfgpetclinic.model.NamedEntity;

import java.util.Optional;

public interface NamedEntityRepository<T extends NamedEntity> {

    Optional<T> findByName(String name);
}
