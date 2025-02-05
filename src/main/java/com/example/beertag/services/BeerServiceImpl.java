package com.example.beertag.services;


import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;
import com.example.beertag.entities.filter.FilterOptions;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.exceptions.UnauthorizedOperationException;
import com.example.beertag.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeerServiceImpl implements BeerService {

    public static final String MODIFY_BEERS = "Only admins or the beer's creator can modify beers!";

    private BeerRepository repository;



    @Autowired
    public BeerServiceImpl(BeerRepository repository){
        this.repository = repository;
    }

    @Override
    public List<Beer> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Beer> get(FilterOptions filterOptions) {
        return repository.get(filterOptions);
    }

    @Override
    public Beer get(int id){
        return repository.get(id);
    }

    @Override
    public Beer get(String name) {
        return repository.get(name);
    }

    @Override
    public void create(Beer beer, User user) {

        boolean duplicateExists = true;

        try{
            repository.get(beer.getName());
        } catch (EntityNotFoundException e){
            duplicateExists = false;
        }

        if (duplicateExists){
            throw new DuplicateEntityException("Beer", "name", beer.getName());
        }

        beer.setCreatedBy(user);
        repository.create(beer);
    }

    @Override
    public void update(Beer beer, User user) {
        checkModifyPermissions(beer.getId(), user);

        boolean duplicateExists = true;

        try {
            Beer existingBeer = repository.get(beer.getName());
            if(existingBeer.getId() == beer.getId()){
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e ){
            duplicateExists = false;
        }

        if (duplicateExists){
            throw new DuplicateEntityException("Beer", "name", beer.getName());
        }

        repository.update(beer);
    }

    @Override
    public void delete(int id, User user) {

        checkModifyPermissions(id, user);

        repository.delete(id);
    }

    public void checkModifyPermissions(int beerId, User user) {
        Beer beer = repository.get(beerId);
        if (!(user.isAdmin() || beer.getCreatedBy().equals(user))) {
            throw new UnauthorizedOperationException(MODIFY_BEERS);
        }
    }
}
