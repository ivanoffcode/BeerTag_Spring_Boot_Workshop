package com.example.beertag.services;

import com.example.beertag.entities.Style;

import java.util.List;

public interface StyleService {

    List<Style> get();

    Style get(int id);

    void create(Style beer);

    void update(Style beer);

    void delete(int id);

}
