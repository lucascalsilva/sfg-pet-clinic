package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import guru.springframework.sfgpetclinic.services.jpa.PetTypeServiceJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;

@RequiredArgsConstructor
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

    private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
    private final PetService petService;
    private final OwnerService ownerService;
    private final PetTypeService petTypeService;

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") String ownerId) {
        return ownerService.findById(Long.valueOf(ownerId));
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }


    @GetMapping("/pets/new")
    public ModelAndView initCreationForm(Owner owner) {
        ModelAndView mav = new ModelAndView();
        Pet pet = Pet.builder().build();
        owner.addPet(pet);

        mav.addObject("pet", pet);
        mav.setViewName(VIEWS_PETS_CREATE_OR_UPDATE_FORM);

        return mav;
    }

    @PostMapping("/pets/new")
    public ModelAndView processCreationForm(Owner owner, @Valid Pet pet, BindingResult result) {
        ModelAndView mav = new ModelAndView();
        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
            result.rejectValue("name", "duplicate", "already exists");
        }
        owner.addPet(pet);
        if (result.hasErrors()) {
            mav.addObject("pet", pet);
            mav.setViewName(VIEWS_PETS_CREATE_OR_UPDATE_FORM);
            return mav;
        }
        else {
            petService.save(pet);
            mav.setViewName("redirect:/owners/" + owner.getId());
            return mav;
        }
    }

    @GetMapping("/pets/{petId}/edit")
    public ModelAndView initUpdateForm(@PathVariable("petId") String petId) {
        ModelAndView mav = new ModelAndView();

        Pet pet = petService.findById(Long.valueOf(petId));

        mav.addObject("pet", pet);
        mav.setViewName(VIEWS_PETS_CREATE_OR_UPDATE_FORM);

        return mav;
    }

    @PostMapping("/pets/{petId}/edit")
    public ModelAndView processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner) {
        ModelAndView mav = new ModelAndView();

        if (result.hasErrors()) {
            pet.setOwner(owner);

            mav.addObject("pet", pet);
            mav.setViewName(VIEWS_PETS_CREATE_OR_UPDATE_FORM);
            return mav;
        }
        else {
            owner.addPet(pet);
            petService.save(pet);

            mav.setViewName("redirect:/owners/" + owner.getId());
            return mav;
        }
    }
}
