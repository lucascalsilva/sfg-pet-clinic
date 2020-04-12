package guru.springframework.sfgpetclinic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static guru.springframework.sfgpetclinic.util.ApplicationConstants.*;

@Controller
@RequestMapping({"", "/", "index", "index.html"})
@RequiredArgsConstructor
public class IndexController {

    @RequestMapping
    public String index(){
        return INDEX_PAGE;
    }

    @RequestMapping(OOUPS_MAPPING)
    public String oupsHandler(){
        return NOT_IMPLEMENTED_PAGE;
    }
}
