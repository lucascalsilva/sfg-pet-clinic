package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.support.discovery.SelectorResolver;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    OwnerService ownerService;

    @Mock
    PetTypeService petTypeService;

    @Mock
    PetService petService;

    @InjectMocks
    PetController petController;

    MockMvc mockMvc;

    Owner owner;
    Set<PetType> petTypes;

    @BeforeEach
    void setup(){
        owner = Owner.builder().id(1L).pets(new HashSet<>()).build();

        petTypes = new HashSet<>();
        petTypes.add(PetType.builder().id(1L).name("DOG").build());
        petTypes.add(PetType.builder().id(2L).name("CAT").build());

        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
    }

    /*@Test
    void populatePetTypes() throws Exception {

    }

    @Test
    void findOwner() {

    }

    @Test
    void initOwnerBinder() {

    }*/

    @Test
    void initCreationForm() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);

        mockMvc.perform(get("/owners/1/pets/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("owner", Matchers.any(Owner.class)))
                .andExpect(model().attribute("pet", Matchers.any(Pet.class)))
                .andExpect(model().attribute("types", Matchers.hasSize(2)))
                .andExpect(view().name("pets/createOrUpdatePetForm"));

        verify(ownerService, times(1)).findById(1L);
        verify(petTypeService, times(1)).findAll();
    }

    @Test
    void processCreationForm() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);

        mockMvc.perform(post("/owners/1/pets/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

        verify(petService, times(1)).save(any());
        verify(ownerService, times(1)).findById(1L);
        verify(petTypeService, times(1)).findAll();

    }

    @Test
    void initUpdateForm() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);
        when(petService.findById(anyLong())).thenReturn(Pet.builder().id(2L).build());

        mockMvc.perform(get("/owners/1/pets/2/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("owner", Matchers.any(Owner.class)))
                .andExpect(model().attribute("pet", Matchers.any(Pet.class)))
                .andExpect(model().attribute("types", Matchers.hasSize(2)))
                .andExpect(view().name("pets/createOrUpdatePetForm"));

        verify(petService, times(1)).findById(2L);
        verify(ownerService, times(1)).findById(1L);
        verify(petTypeService, times(1)).findAll();
    }

    @Test
    void processUpdateForm() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);

        mockMvc.perform(post("/owners/1/pets/2/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

        verify(petService).save(any());
        verify(ownerService, times(1)).findById(1L);
        verify(petTypeService, times(1)).findAll();
    }
}