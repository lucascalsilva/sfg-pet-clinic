package guru.springframework.sfgpetclinic.services.jpa;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.repositories.PetTypeRepository;
import guru.springframework.sfgpetclinic.services.map.OwnerServiceMap;
import guru.springframework.sfgpetclinic.services.map.PetServiceMap;
import guru.springframework.sfgpetclinic.services.map.PetTypeServiceMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceJPATest {

    @InjectMocks
    private OwnerServiceJPA ownerServiceJPA;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PetTypeRepository petTypeRepository;

    @Mock
    private PetRepository petRepository;

    private final Long OWNER_ID = 1L;
    private final String LAST_NAME = "Smith";
    private final String PET_NAME = "Garurinho";
    private final String PET_TYPE_NAME = "Cat";
    private Owner owner;

    @BeforeEach
    void setup(){
        Pet pet = Pet.builder().petType(PetType.builder().name(PET_TYPE_NAME).build())
                .name(PET_NAME).build();
        Set<Pet> pets = new HashSet<>();
        pets.add(pet);

        owner = Owner.builder().id(OWNER_ID).lastName(LAST_NAME).pets(pets).build();
    }

    @Test
    void findByLastName() {
        when(ownerRepository.findByLastNameLikeIgnoreCase(anyString()))
                .thenReturn(Arrays.asList(owner));

        assertThat(ownerServiceJPA.findByLastName(LAST_NAME)).contains(owner);
        verify(ownerRepository, times(1)).findByLastNameLikeIgnoreCase(eq(LAST_NAME));
    }

    @Test
    void findById() {
        when(ownerRepository.findById(anyLong()))
                .thenReturn(Optional.of(owner));

        assertThat(ownerServiceJPA.findById(OWNER_ID)).isEqualTo(owner);
        verify(ownerRepository, times(1)).findById(eq(OWNER_ID));
    }

    @Test
    void delete() {
        ownerServiceJPA.delete(owner);
        verify(ownerRepository, times(1)).delete(any());
    }

    @Test
    void deleteById() {
        ownerServiceJPA.deleteById(OWNER_ID);
        verify(ownerRepository, times(1)).deleteById(eq(OWNER_ID));
    }

    @Test
    void findAll() {
        Set<Owner> owners = new HashSet<>();
        owners.add(owner);
        when(ownerRepository.findAll()).thenReturn(owners);

        assertThat(ownerServiceJPA.findAll()).contains(owner);
        verify(ownerRepository, times(1)).findAll();
    }
}