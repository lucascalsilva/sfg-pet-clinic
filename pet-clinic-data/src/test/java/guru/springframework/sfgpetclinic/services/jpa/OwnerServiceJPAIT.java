package guru.springframework.sfgpetclinic.services.jpa;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.repositories.PetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class OwnerServiceJPAIT {

    private OwnerServiceJPA ownerServiceJPA;

    @Autowired
    private EntityManager testEntityManager;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Autowired
    private PetRepository petRepository;

    private final Long ID = 1L;
    private final String LAST_NAME = "Smith";
    private Long ownerId;
    private Owner owner;

    @BeforeEach
    void setup(){
        ownerServiceJPA = new OwnerServiceJPA(ownerRepository, petTypeRepository, petRepository);

        PetType catType = PetType.builder().name("Cat").build();

        petTypeRepository.save(catType);

        Pet cat1 = Pet.builder().petType(catType)
                .name("Garurinho").build();

        Pet cat2 = Pet.builder().petType(catType)
                .name("Diruri").build();

        Set<Pet> pets = new HashSet<>();
        pets.add(cat1);
        pets.add(cat2);

        owner = ownerServiceJPA.save(Owner.builder().lastName(LAST_NAME).pets(pets).build());
        ownerId = owner.getId();
    }

    @Test
    void findByLastName() {
        Set<Owner> ownerByLastName = ownerServiceJPA.findByLastName(LAST_NAME);

        assertThat(ownerByLastName).isNotEmpty();
        assertThat(ownerByLastName).contains(owner);
    }

    @Test
    void findById() {
        Owner ownerById = ownerServiceJPA.findById(ownerId);

        assertThat(ownerById).isNotNull();
        assertThat(ownerById).isEqualTo(owner);
    }

    @Test
    void delete() {
        ownerServiceJPA.delete(owner);
        assertThat(ownerServiceJPA.findAll()).isEmpty();
    }

    @Test
    void deleteById() {
        ownerServiceJPA.deleteById(ownerId);
        assertThat(ownerServiceJPA.findAll()).isEmpty();
    }

    @Test
    void findAll() {
        assertThat(ownerServiceJPA.findAll()).isNotEmpty();
    }
}