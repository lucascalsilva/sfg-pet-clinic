package guru.springframework.sfgpetclinic.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgpetclinic.model.*;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import guru.springframework.sfgpetclinic.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private final OwnerService ownerService;
    private final PetService petService;
    private final PetTypeService petTypeService;
    private final SpecialtyService specialtyService;
    private final VetService vetService;
    private final VisitService visitService;
    private final DataLoaderConfig dataLoaderConfig;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadPetTypes();
        loadOwners();
        loadSpecialities();
        loadVets();
        loadVisits();
    }

    private void loadPetTypes() throws IOException {
        File file = loadFile("1-petTypes.json");
        PetType[] petTypes = mapper.readValue(file, PetType[].class);

        Arrays.stream(petTypes).forEach(petTypeService::save);
    }

    private void loadOwners() throws IOException {
        File file = loadFile("2-owners.json");
        Owner[] owners = mapper.readValue(file, Owner[].class);

        Arrays.stream(owners).forEach(owner -> {

            owner.getPets().stream().forEach(pet -> {
                pet.setPetType(petTypeService.findByName(pet.getPetType().getName()));
            });

            ownerService.save(owner);
        });
    }

    private void loadSpecialities() throws IOException {
        File file = loadFile("3-specialties.json");
        Specialty[] specialties = mapper.readValue(file, Specialty[].class);

        Arrays.stream(specialties).forEach(specialtyService::save);
    }

    private void loadVets() throws IOException {
        File file = loadFile("4-vets.json");
        Vet[] vets = mapper.readValue(file, Vet[].class);

        Arrays.stream(vets).forEach(vet -> {
            Set<Specialty> specialties = vet.getSpecialties().stream().map(specialty -> specialtyService.findByName(specialty.getName())).collect(Collectors.toSet());
            vet.setSpecialties(specialties);

            vetService.save(vet);
        });
    }

    private void loadVisits() throws IOException {
        File file = loadFile("5-visits.json");
        Visit[] visits = mapper.readValue(file, Visit[].class);

        Arrays.stream(visits).forEach(visit -> {
            Pet pet = petService.findByNameAndBirthDate(visit.getPet().getName(), visit.getPet().getBirthDate());
            visit.setPet(pet);

            visitService.save(visit);
        });
    }

    private File loadFile(String fileName){
        URL fileUrl = Thread.currentThread().getContextClassLoader().getResource(dataLoaderConfig.getBootstrapFilesFolder() + "/" + fileName);

        if (fileUrl != null) {
            Optional<File> file = Optional.of(new File(fileUrl.getPath()));

            return file.orElseThrow(() -> new RuntimeException("File with file name "+ fileName + " not found."));

        } else {
            log.warn("Bootstrap folder not found...");
            throw new RuntimeException("File url not found for file name "+fileName);
        }
    }
}
