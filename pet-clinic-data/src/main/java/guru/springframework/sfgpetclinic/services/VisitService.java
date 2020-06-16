package guru.springframework.sfgpetclinic.services;

import guru.springframework.sfgpetclinic.model.Visit;

import java.util.Optional;
import java.util.Set;

public interface VisitService extends CrudService<Visit, Long> {

    Set<Visit> findByPetId(Long petId);


}
