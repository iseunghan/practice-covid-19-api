package me.iseunghan.practicecovid19.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "redirect:/covid19";
    }

    @GetMapping("/covid19")
    public String covid19_Page(Model model){
        model.addAttribute("text", "hello");
        return "/covid19/covid19Page.html";
    }
}
