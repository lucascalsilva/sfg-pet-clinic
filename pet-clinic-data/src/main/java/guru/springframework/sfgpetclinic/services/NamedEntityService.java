package guru.springframework.sfgpetclinic.services;

import guru.springframework.sfgpetclinic.model.NamedEntity;

public interface NamedEntityService<T extends NamedEntity> {

    T findByName(String name);
}
