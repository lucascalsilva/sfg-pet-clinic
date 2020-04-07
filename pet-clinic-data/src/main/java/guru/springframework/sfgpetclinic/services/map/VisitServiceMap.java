package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Profile({"map", "default"})
@RequiredArgsConstructor
public class VisitServiceMap extends AbstractMapService<Visit, Long> implements VisitService {

    @Override
    public Set<Visit> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    @Override
    public void delete(Visit object) {
        super.delete(object);
    }

    @Override
    public Visit save(Visit object) {
        if(object != null) {
            if(object.getPet() != null && object.getPet().getId() != null && object.getPet().getOwner() != null
                    && object.getPet().getOwner().getId() != null){
                return super.save(object);
            }
            else{
                throw new RuntimeException("Invalid Pet information in the Visit record.");
            }
        }
        else{
            return null;
        }
    }

    @Override
    public Visit findById(Long id) {
        return super.findById(id);
    }
}
