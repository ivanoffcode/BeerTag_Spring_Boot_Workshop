package com.example.beertag.repositories;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.filter.FilterOptions;

import java.util.List;

public interface BeerRepository {

    List<Beer>getAll();

    List<Beer> get(FilterOptions filterOptions);

    Beer get(int id);

    Beer get(String name);

    void create(Beer beer);

    void update(Beer beer);

    void delete(int id);

}
