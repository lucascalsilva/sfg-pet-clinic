package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final PetService petService;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    /**
     * Called before each and every @RequestMapping annotated method. 2 goals: - Make sure
     * we always have fresh data - Since we do not use the session scope, make sure that
     * Pet object always has an id (Even though id is not part of the form fields)
     * @param petId
     * @return Pet
     */
    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("petId") String petId, Model model) {
        Long petId_ = Long.valueOf(petId);
        Pet pet = petService.findById(petId_);
        pet.setVisits(visitService.findByPetId(petId_));
        Visit visit = Visit.builder().build();
        pet.addVisit(visit);

        model.addAttribute("pet", pet);

        return visit;
    }

    // Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
    @GetMapping("/owners/*/pets/{petId}/visits/new")
    public ModelAndView initNewVisitForm(@PathVariable("petId") int petId, Model model) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("pets/createOrUpdateVisitForm");
        mav.addAllObjects(model.asMap());
        return mav;
    }

    // Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
    @PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    public ModelAndView processNewVisitForm(@Valid Visit visit, BindingResult result) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("pets/createOrUpdateVisitForm");
            return mav;
        }
        else {
            visitService.save(visit);
            mav.setViewName("redirect:/owners/"+visit.getPet().getOwner().getId());
            return mav;
        }
    }

}
