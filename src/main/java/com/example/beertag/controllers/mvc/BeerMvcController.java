package com.example.beertag.controllers.mvc;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.Brewery;
import com.example.beertag.entities.Style;
import com.example.beertag.entities.User;
import com.example.beertag.entities.dtos.BeerDto;
import com.example.beertag.exceptions.AuthenticationFailureException;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.exceptions.UnauthorizedOperationException;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.mappers.BeerMapper;
import com.example.beertag.services.BeerService;
import com.example.beertag.services.BreweryService;
import com.example.beertag.services.StyleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/beers")
public class BeerMvcController {

    private final BeerService beerService;
    private final StyleService styleService;
    private final BreweryService breweryService;
    private final BeerMapper beerMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public BeerMvcController(BeerService beerService,
                             StyleService styleService,
                             BreweryService breweryService,
                             BeerMapper beerMapper,
                             AuthenticationHelper authenticationHelper) {
        this.beerService = beerService;
        this.styleService = styleService;
        this.breweryService = breweryService;
        this.beerMapper = beerMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session){
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/{id}")
    public String showBeer(@PathVariable int id, Model model) {
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
    public String showAllBeers(Model model) {
        try {
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

    @GetMapping("/new")
    public String showNewBeerPage(Model model, HttpSession session) {
        try{
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("beer", new BeerDto());
        return "beer-new";
    }

    @ModelAttribute("styles")
    public List<Style> populateStyles() {
        return styleService.getAll();
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request){
        return request.getRequestURI();
    }

    @ModelAttribute("breweries")
    public List<Brewery> populateBreweries() {
        return breweryService.getAll();
    }

    @PostMapping("/new")
    public String createBeer(@Valid @ModelAttribute("beer") BeerDto beer,
                             BindingResult errors, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "beer-new";
        }
        try {
            Beer newBeer = beerMapper.fromDto(beer, user);
            beerService.create(newBeer, user);
            return "redirect:/beers";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "beer.exists", e.getMessage());
            return "beer-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditBeerPage(@PathVariable int id, Model model, HttpSession session){
        try{
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try{
            Beer beer = beerService.get(id);
            BeerDto beerDto = beerMapper.toDto(beer);
            model.addAttribute("beerId", id);
            model.addAttribute("beer", beerDto);
            return "beer-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateBeer(@PathVariable int id, @Valid @ModelAttribute("beer") BeerDto beer,
                             BindingResult errors, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "beer-update";
        }
        try {
            Beer newBeer = beerMapper.fromDto(id, beer);
            beerService.update(newBeer, user);
            return "redirect:/beers";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "beer.exists", e.getMessage());
            return "beer-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("status", HttpStatus.FORBIDDEN.value()
                    + " " + HttpStatus.FORBIDDEN.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteBeer(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            beerService.delete(id, user);
            return "redirect:/beers";
        }
        catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.value()
                    + " " + HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("status", HttpStatus.FORBIDDEN.value()
                    + " " + HttpStatus.FORBIDDEN.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }
}
