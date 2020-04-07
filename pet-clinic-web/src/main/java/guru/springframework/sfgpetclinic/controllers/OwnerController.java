package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.services.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/owners.html", "/owners", "/owners/index", "/owners/index.html"})
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @RequestMapping
    public String listOwners(Model model){
        model.addAttribute("owners", ownerService.findAll());
        return "owners/owners";
    }

    @RequestMapping("/find")
    public String findOwners(Model model){
        return "notimplemented";
    }
}
