package com.example.beertag.repositories;

import com.example.beertag.entities.Brewery;

import java.util.List;

public interface BreweryRepository {

    List<Brewery> get();

    Brewery get(int id);

    Brewery get(String name);

    void create(Brewery brewery);

    void update(Brewery brewery);

    void delete(int id);




}
