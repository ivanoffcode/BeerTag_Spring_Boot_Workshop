package com.example.beertag.services;

import com.example.beertag.entities.Brewery;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.repositories.BreweryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BreweryServiceImpl implements BreweryService{

    private BreweryRepository repository;

    @Autowired
    public BreweryServiceImpl(BreweryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Brewery> getAll() {
        return repository.get();
    }

    @Override
    public Brewery getByID(int id) {
        return repository.get(id);
    }

    @Override
    public void create(Brewery brewery) {
        boolean breweryExists = true;
        try {
          Brewery existingBrewery = repository.get(brewery.getName());
        } catch (EntityNotFoundException e){
            breweryExists = false;
        }
        if(breweryExists) {
            throw new DuplicateEntityException("Brewery", "name", brewery.getName());
        }

        repository.create(brewery);
    }

    @Override
    public void update(Brewery brewery) {
        boolean duplicateExists = true;

        try {
            Brewery existingBeer = repository.get(brewery.getName());
            if(existingBeer.getId() == brewery.getId()){
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e ){
            duplicateExists = false;
        }

        if (duplicateExists){
            throw new DuplicateEntityException("Brewery", "name", brewery.getName());
        }

        repository.update(brewery);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
