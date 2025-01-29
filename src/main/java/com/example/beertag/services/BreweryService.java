package com.example.beertag.services;

import com.example.beertag.entities.Brewery;

import java.util.List;

public interface BreweryService {

    List<Brewery> getAll();

    Brewery getByID(int id);

    void create(Brewery brewery);

    void update(Brewery brewery);

    void delete(int id);

}
