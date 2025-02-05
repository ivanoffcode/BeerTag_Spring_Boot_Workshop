package com.example.beertag.services;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;
import com.example.beertag.entities.filter.FilterOptions;

import java.util.List;

public interface BeerService {

    List<Beer>getAll();

    List<Beer> get(FilterOptions filterOptions);

    Beer get(int id);

    Beer get(String name);

    void create(Beer beer, User user);

    void update(Beer beer, User user);

    void delete(int id, User user);
}
