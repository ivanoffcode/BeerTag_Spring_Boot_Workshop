package com.example.beertag.services;

import com.example.beertag.entities.Style;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.repositories.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleServiceImpl implements StyleService {

    private final StyleRepository styleRepository;

    @Autowired
    public StyleServiceImpl(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    @Override
    public List<Style> get() {
        return styleRepository.get();
    }

    @Override
    public Style get(int id) {
        return styleRepository.get(id);
    }

    @Override
    public void create(Style style) {
        boolean duplicateExists = true;

        try {
            styleRepository.get(style.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Style", "name", style.getName());
        }

        styleRepository.create(style);
    }

    @Override
    public void update(Style style) {
        try {
            Style existingStyle = styleRepository.get(style.getName());

            if (existingStyle.getId() != style.getId()) {
                throw new DuplicateEntityException("Style", "name", style.getName());
            }

        } catch (EntityNotFoundException e) {

        }

        styleRepository.update(style);
    }

    @Override
    public void delete(int id) {
        styleRepository.delete(id);
    }

}
