package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Vet;
import guru.springframework.sfgpetclinic.services.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    @GetMapping({"/vets", "/vets.html", "/vets/index", "/vets/index.html"})
    public String listVets(Model model){
        model.addAttribute("vets", vetService.findAll());
        return "vets/vets";
    }

    @GetMapping(path = "/api/vets")
    public @ResponseBody Set<Vet> getVetsJson(){
        return vetService.findAll();
    }
}
