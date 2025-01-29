package com.example.beertag.services;

import com.example.beertag.entities.Country;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService{

    private CountryRepository repository;

    @Autowired
    public CountryServiceImpl(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Country> get() {
        return repository.get();
    }

    @Override
    public Country get(int id) {
        return repository.get(id);
    }

    @Override
    public void create(Country country) {
        boolean duplicateExists = true;

        try{
            repository.get(country.getName());
        } catch (EntityNotFoundException e){
            duplicateExists = false;
        }

        if (duplicateExists){
            throw new DuplicateEntityException("Country", "name", country.getName());
        }

        repository.create(country);
    }

    @Override
    public void update(Country country) {
        boolean duplicateExists = true;

        try {
            Country existingCountry = repository.get(country.getName());
            if(existingCountry.getId() == country.getId()){
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e ){
            duplicateExists = false;
        }

        if (duplicateExists){
            throw new DuplicateEntityException("Country", "name", country.getName());
        }

        repository.update(country);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
