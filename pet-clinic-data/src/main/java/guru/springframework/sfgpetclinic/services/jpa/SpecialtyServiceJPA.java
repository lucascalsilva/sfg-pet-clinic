package guru.springframework.sfgpetclinic.services.jpa;

import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.model.Specialty;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import guru.springframework.sfgpetclinic.services.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class SpecialtyServiceJPA implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    @Override
    public Set<Specialty> findAll() {
        Set<Specialty> specialties = new HashSet<Specialty>();
        specialtyRepository.findAll().iterator().forEachRemaining(specialties::add);
        return specialties;
    }

    @Override
    public Specialty findById(Long aLong) {
        return specialtyRepository.findById(aLong).orElse(null);
    }

    @Override
    public Specialty save(Specialty object) {
        return specialtyRepository.save(object);
    }

    @Override
    public void delete(Specialty object) {
        specialtyRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        specialtyRepository.deleteById(aLong);
    }

    @Override
    public Specialty findByName(String name) {
        return specialtyRepository.findByName(name).orElse(null);
    }
}
