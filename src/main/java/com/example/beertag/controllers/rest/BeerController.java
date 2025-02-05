package com.example.beertag.controllers.rest;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;
import com.example.beertag.entities.dtos.BeerDto;
import com.example.beertag.entities.filter.FilterOptions;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.exceptions.UnauthorizedOperationException;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.mappers.BeerMapper;
import com.example.beertag.services.BeerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerController {

    public static final String UNAUTHORIZED_CREATE_BEER_MESSAGE = "Invalid username or password, only existing users can create new beers!";
    public static final String UNAUTHORIZED_UPDATE_BEER_MESSAGE = "Invalid username or password, only admins and beer's creator can modify the beer!";
    public static final String UNAUTHORIZED_DELETE_BEER_MESSAGE = "Invalid username or password, only admins and beer's creator can delete the beer";
    public static final String BEER_ID_NOT_FOUND = "Beer with id %d not found.";
    public static final String BEER_NAME_NOT_FOUND = "Beer with name %s not found.";

    private final BeerService service;
    private final BeerMapper beerMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public BeerController(BeerService service, BeerMapper beerMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.beerMapper = beerMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Beer> get(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minAbv,
            @RequestParam(required = false) Double maxAbv,
            @RequestParam(required = false) Integer styleId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        FilterOptions filterOptions = new FilterOptions(name, minAbv, maxAbv, styleId, sortBy, sortOrder);
        return service.get(filterOptions);
    }

    @GetMapping("/{id}")
    public Beer getById(@PathVariable int id){
        try {
            return service.get(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, String.format(BEER_ID_NOT_FOUND, id));
        }
    }

    @GetMapping("/search")
    public Beer getByName(@RequestParam String name){
        try {
            return service.get(name);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, String.format(BEER_NAME_NOT_FOUND, name));
        }
    }

    @PostMapping
    public Beer create(@RequestHeader HttpHeaders headers, @Valid @RequestBody BeerDto beerDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Beer beerToCreate = beerMapper.fromDto(beerDto);
            service.create(beerToCreate, user);
            return beerToCreate;

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    UNAUTHORIZED_CREATE_BEER_MESSAGE);
        }

    }

    @PutMapping("/{id}")
    public Beer update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody BeerDto beerDto) {

       try {
           User user = authenticationHelper.tryGetUser(headers);
           Beer beerToUpdate = beerMapper.fromDto(id, beerDto);
           service.update(beerToUpdate, user);
           return beerToUpdate;
       } catch (EntityNotFoundException e) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
       } catch (DuplicateEntityException e) {
           throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
       } catch (UnauthorizedOperationException e) {
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                   UNAUTHORIZED_UPDATE_BEER_MESSAGE);
       }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
       try {
           User user = authenticationHelper.tryGetUser(headers);
           service.delete(id, user);
       } catch (EntityNotFoundException e){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
       } catch (UnauthorizedOperationException e) {
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                   UNAUTHORIZED_DELETE_BEER_MESSAGE);
       }
    }


}
