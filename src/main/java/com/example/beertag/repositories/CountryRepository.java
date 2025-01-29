package com.example.beertag.repositories;


import com.example.beertag.entities.Country;

import java.util.List;

public interface CountryRepository {

    List<Country> get();

    Country get(int id);

    Country get(String name);

    void create(Country country);

    void update(Country country);

    void delete(int id);

}
