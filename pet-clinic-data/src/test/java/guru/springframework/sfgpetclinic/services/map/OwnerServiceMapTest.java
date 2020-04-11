package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class OwnerServiceMapTest {

    private final Long ID = 1L;
    private final String LAST_NAME = "Smith";
    private final String PET_NAME = "Garurinho";
    private final String PET_TYPE_NAME = "Cat";
    private Owner owner;
    private Pet pet;
    private OwnerServiceMap ownerServiceMap;


    @BeforeEach
    void setUp() {
        ownerServiceMap = new OwnerServiceMap(new PetTypeServiceMap(), new PetServiceMap());
        pet = Pet.builder().petType(PetType.builder().name(PET_TYPE_NAME).build())
                .name(PET_NAME).build();
        Set<Pet> pets = new HashSet<>();
        pets.add(pet);

        owner = Owner.builder().id(ID).lastName(LAST_NAME).pets(pets).build();
        ownerServiceMap.save(owner);
    }

    @Test
    void saveNoId(){
        Pet petOwnerNoId = Pet.builder().petType(PetType.builder().name(PET_TYPE_NAME)
                .build()).name("Diruri").build();
        Set<Pet> pets = new HashSet<>();
        pets.add(petOwnerNoId);

        Owner ownerNoId = Owner.builder().lastName("Prichard").pets(pets).build();
        Owner savedOwner = ownerServiceMap.save(ownerNoId);

        assertThat(savedOwner.getId()).isNotNull();
        assertThat(ownerServiceMap.findAll()).contains(savedOwner);
    }

    @Test
    void findAll() {
        Set<Owner> owners = ownerServiceMap.findAll();
        assertThat(owners).contains(owner);
    }

    @Test
    void deleteById() {
        ownerServiceMap.deleteById(ID);
        assertThat(ownerServiceMap.findAll()).isEmpty();
    }

    @Test
    void delete() {
        ownerServiceMap.delete(owner);
        assertThat(ownerServiceMap.findAll()).isEmpty();
    }

    @Test
    void findByLastName() {
        Set<Owner> ownersByLastName = ownerServiceMap.findByLastName(LAST_NAME);
        assertThat(ownersByLastName).contains(owner);
    }

    @Test
    void testNoFindByLastName() {
        Set<Owner> ownersByLastName = ownerServiceMap.findByLastName("foo");
        assertThat(ownersByLastName).doesNotContain(owner);
    }

    @Test
    void testFindById() {
        Owner ownerById = ownerServiceMap.findById(ID);
        assertThat(ownerById).isNotNull();
        assertThat(ownerById).isEqualTo(owner);
    }
}