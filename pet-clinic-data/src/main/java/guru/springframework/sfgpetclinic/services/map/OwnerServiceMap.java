package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile({"map", "default"})
@RequiredArgsConstructor
public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements OwnerService {

    private final PetTypeService petTypeService;
    private final PetService petService;

    @Override
    public Set<Owner> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    @Override
    public void delete(Owner object) {
        super.delete(object);
    }

    @Override
    public Owner save(Owner object) {
        if (object != null) {
            object.getPets().forEach(pet -> {
                if (pet.getPetType() != null && pet.getPetType().getId() == null) {
                    pet.setPetType(petTypeService.save(pet.getPetType()));
                } else {
                    throw new RuntimeException("Pet type is required");
                }
                if (pet.getId() == null) {
                    pet.setId(petService.save(pet).getId());
                    pet.setOwner(object);
                }
            });
            return super.save(object);
        } else {
            return null;
        }
    }

    @Override
    public Set<Owner> findByLastName(String lastName) {
        return findAll().stream()
                .filter(owner -> owner.getLastName().contains(lastName))
                .collect(Collectors.toSet());
    }

    @Override
    public Owner findById(Long id) {
        return super.findById(id);
    }
}
