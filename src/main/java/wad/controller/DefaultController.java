package wad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/") //redirects to "front page"
    public String redirect() {
        return "redirect:/news";
    }
}
