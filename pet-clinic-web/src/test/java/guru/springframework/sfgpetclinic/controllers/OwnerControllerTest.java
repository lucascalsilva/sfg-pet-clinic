package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private OwnerController ownerController;

    private Set<Owner> owners;

    private MockMvc mockMvc;

    private final String[] urls = {"/owners", "/owners.html", "/owners/index", "/owners/index.html"};


    @BeforeEach
    void setUp(){
        PetType petType = PetType.builder().id(1L).name("Dog").build();
        Pet pet = Pet.builder().id(1L).name("Catioro").petType(petType).build();
        Owner owner = Owner.builder().id(1L).firstName("Lucas").lastName("Silva")
                .pets(new HashSet<>()).build();
        owner.addPet(pet);
        owners = new HashSet<>();
        owners.add(owner);

        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @Test
    void findAll() throws Exception {
        when(ownerService.findAll()).thenReturn(owners);

        for(String url : urls) {
            mockMvc.perform(get(url))
                    .andExpect(status().is(200))
                    .andExpect(view().name("owners/owners"))
                    .andExpect(model().attribute("owners", hasSize(1)));
            ;
        }

        verify(ownerService, times(urls.length)).findAll();
    }

    @Test
    void findOwners() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().is(200))
                .andExpect(view().name("notimplemented"));

        verify(ownerService, times(0)).findAll();
    }
}