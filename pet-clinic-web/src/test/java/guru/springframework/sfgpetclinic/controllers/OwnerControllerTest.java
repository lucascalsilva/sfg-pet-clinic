package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.typeCompatibleWith;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private OwnerController ownerController;

    private MockMvc mockMvc;

    private final String[] urls = {"/owners", "/owners.html", "/owners/index", "/owners/index.html"};


    @BeforeEach
    void setUp(){

        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @Test
    void findOwnersAllUrls() throws Exception {
        when(ownerService.findByLastName(anyString())).thenReturn(Set.of());

        for(String url : urls) {
            mockMvc.perform(get(url)
                    .param("lastName", "123"))
                    .andExpect(status().is(200))
                    .andExpect(view().name("owners/findOwners"));
        }

        verify(ownerService, times(urls.length)).findByLastName(anyString());
    }

    @Test
    void findOwners() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().is(200))
                .andExpect(view().name("owners/findOwners"))
                .andExpect(model().attribute("owner", Matchers.any(Owner.class)));

        verifyNoInteractions(ownerService);
    }

    @Test
    void processFindFormReturnMany() throws Exception {
        Owner owner1 = Owner.builder().id(1L).build();
        Owner owner2 = Owner.builder().id(2L).build();

        when(ownerService.findByLastName(anyString())).thenReturn(Set.of(owner1, owner2));

        mockMvc.perform(get("/owners")
                .param("lastName", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attribute("owners", hasSize(2)))
                .andExpect(model().attribute("owners", Matchers.any(Set.class)));

    }

    @Test
    void processFindFormReturnOne() throws Exception {
        Owner owner = Owner.builder().id(1L).build();

        when(ownerService.findByLastName(anyString())).thenReturn(Set.of(owner));

        mockMvc.perform(get("/owners")
                .param("lastName", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(model().attributeExists("owner"));
    }

    @Test
    void showOwner() throws Exception{
        Owner owner = Owner.builder().id(1L).build();

        when(ownerService.findById(1L)).thenReturn(owner);

        mockMvc.perform(get("/owners/1"))
                .andExpect(status().is(200))
                .andExpect(model().attribute("owner", hasProperty("id", is(1L))))
                .andExpect(view().name("owners/ownerDetails"));

        verify(ownerService, times(1)).findById(1L);
        verify(ownerService, times(0)).findAll();
    }

    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attribute("owner", Matchers.any(Owner.class)));

        verifyNoInteractions(ownerService);
    }

    @Test
    void processNewOwnerForm() throws Exception{
        String firstName = "Some First Name";
        String lastName = "Some Last Name";
        Owner owner = Owner.builder().id(1L).firstName(firstName).lastName(lastName).build();

        ArgumentCaptor<Owner> argumentCaptor = ArgumentCaptor.forClass(Owner.class);
        when(ownerService.save(any())).thenReturn(owner);

        mockMvc.perform(post("/owners/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", firstName)
                .param("lastName", lastName))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(model().attribute("owner", Matchers.any(Owner.class)));

        verify(ownerService).save(argumentCaptor.capture());
        Owner saveParamOwner = argumentCaptor.getValue();

        assertThat(saveParamOwner).isNotNull();
        assertThat(saveParamOwner.getFirstName()).isEqualTo(firstName);
        assertThat(saveParamOwner.getLastName()).isEqualTo(lastName);
    }

    @Test
    void processUpdateOwnerForm() throws Exception{
        String firstName = "Some First Name";
        String lastName = "Some Last Name";
        Owner owner = Owner.builder().id(1L).firstName(firstName).lastName(lastName).build();

        ArgumentCaptor<Owner> argumentCaptor = ArgumentCaptor.forClass(Owner.class);
        when(ownerService.save(any())).thenReturn(owner);

        mockMvc.perform(post("/owners/1/edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", firstName)
                .param("lastName", lastName))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(model().attribute("owner", Matchers.any(Owner.class)));

        verify(ownerService).save(argumentCaptor.capture());
        Owner saveParamOwner = argumentCaptor.getValue();

        assertThat(saveParamOwner).isNotNull();
        assertThat(saveParamOwner.getFirstName()).isEqualTo(firstName);
        assertThat(saveParamOwner.getLastName()).isEqualTo(lastName);
    }

    @Test
    void initUpdateForm() throws Exception {
        when(ownerService.findById(any())).thenReturn(Owner.builder().id(1L).build());

        mockMvc.perform(get("/owners/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attribute("owner", Matchers.any(Owner.class)));

        verify(ownerService, times(1)).findById(1L);

    }
}