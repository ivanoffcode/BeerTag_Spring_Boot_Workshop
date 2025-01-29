package com.example.beertag.repositories;

import com.example.beertag.entities.Style;

import java.util.List;

public interface StyleRepository {

    List<Style> get();

    Style get(int id);

    Style get(String name);

    void create(Style style);

    void update(Style style);

    void delete(int id);
}
