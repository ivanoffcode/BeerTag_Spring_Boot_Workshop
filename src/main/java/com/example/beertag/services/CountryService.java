package com.example.beertag.services;

import com.example.beertag.entities.Country;

import java.util.List;

public interface CountryService {

    List<Country> get();

    Country get(int id);

    void create(Country country);

    void update(Country country);

    void delete(int id);
}
