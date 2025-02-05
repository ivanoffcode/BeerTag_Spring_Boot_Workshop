package com.example.beertag.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
public class AboutMvcController {

    @GetMapping
    public String showAboutPage() {
        return "AboutView";
    }
}
