package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Profile({"map", "default"})
@RequiredArgsConstructor
public class PetTypeServiceMap extends AbstractMapService<PetType, Long> implements PetTypeService {

    @Override
    public Set<PetType> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    @Override
    public void delete(PetType object) {
        super.delete(object);
    }

    @Override
    public PetType save(PetType object) {
        PetType savedPetType = findByName(object.getName());
        if(savedPetType == null) {
            return super.save(object);
        }
        else {
            return savedPetType;
        }
    }

    @Override
    public PetType findById(Long id) {
        return super.findById(id);
    }

    public PetType findByName(String name){
        return super.map.values().stream()
                .filter(petType_ -> petType_.getName()
                        .equals(name)).findFirst().orElse(null);
    }
}
