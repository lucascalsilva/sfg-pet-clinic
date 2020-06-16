package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.services.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping({"/owners.html", "/owners", "/owners/index", "/owners/index.html"})
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");

    }

    @RequestMapping("/find")
    public String findOwners(Model model){
        model.addAttribute("owner", Owner.builder().build());
        return "owners/findOwners";
    }

    @GetMapping
    public ModelAndView processFindForm(Owner owner, BindingResult result) {
        ModelAndView mav = new ModelAndView();

        if(owner.getLastName() == null || owner.getLastName().length() < 3){
            result.rejectValue("lastName", "lessThanMinChars", "please provide at least 3 characters");
            mav.setViewName("owners/findOwners");
            return mav;
        }

        // find owners by last name
        Set<Owner> results = this.ownerService.findByLastName(owner.getLastName());
        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found");
            mav.setViewName("owners/findOwners");
        }
        else if (results.size() == 1) {
            // 1 owner found
            owner = results.iterator().next();
            mav.setViewName("redirect:/owners/" + owner.getId());
        }
        else {
            // multiple owners found
            mav.addObject("owners", results);
            mav.setViewName("owners/ownersList");
        }

        return mav;
    }

    @GetMapping("/{ownerId}")
    public ModelAndView showOwner(@PathVariable("ownerId") Long ownerId) {
        ModelAndView mav = new ModelAndView("owners/ownerDetails");
        mav.addObject(ownerService.findById(ownerId));
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView initCreationForm(){
        ModelAndView mav = new ModelAndView("owners/createOrUpdateOwnerForm");
        mav.addObject(Owner.builder().build());
        return mav;
    }

    @PostMapping("/new")
    public ModelAndView processNewOwnerForm(@ModelAttribute Owner owner, BindingResult result){
        return saveOrUpdate(owner, result);
    }

    @GetMapping("/{orderId}/edit")
    public ModelAndView initEditForm(@PathVariable String orderId){
        ModelAndView mav = new ModelAndView("owners/createOrUpdateOwnerForm");
        mav.addObject(ownerService.findById(Long.valueOf(orderId)));
        return mav;
    }

    @PostMapping("/{ownerId}/edit")
    public ModelAndView processUpdateOwnerForm(@ModelAttribute Owner owner, @PathVariable String ownerId, BindingResult result){
        owner.setId(Long.valueOf(ownerId));
        return saveOrUpdate(owner, result);
    }

    private ModelAndView saveOrUpdate(Owner owner, BindingResult result){
        ModelAndView mav = new ModelAndView();

        if(result.hasErrors()) {
            mav.setViewName("owners/createOrUpdateOwnerForm");
            return mav;
        }

        Owner savedOwner = ownerService.save(owner);
        mav.setViewName("redirect:/owners/" + savedOwner.getId());
        mav.addObject(savedOwner);

        return mav;
    }
}
