package com.example.beertag.controllers.mvc;

import com.example.beertag.entities.Beer;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.services.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/beers")
public class BeerMvcController {

    private final BeerService beerService;

    public BeerMvcController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping("/{id}")
    public String showBeer(@PathVariable int id, Model model){
        try {
            Beer beer = beerService.get(id);
            model.addAttribute("beer", beer);
            return "beer";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("status", HttpStatus.NOT_FOUND.value()
                    + " " + HttpStatus.NOT_FOUND.getReasonPhrase());
            return "ErrorView";
        }
    }

    @GetMapping
    public String showAllBeers(Model model){
        try{
            List<Beer> beers = beerService.getAll();
            model.addAttribute("beers", beers);
            return "BeersView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("status", HttpStatus.NOT_FOUND.value()
                    + " " + HttpStatus.NOT_FOUND.getReasonPhrase());
            return "ErrorView";
        }
    }
}
