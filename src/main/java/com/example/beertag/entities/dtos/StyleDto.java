package com.example.beertag.entities.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StyleDto {

    @NotNull(message = "Name can't be empty")
    @Size(min = 2, max = 20, message = "Name should be between 2 and 20 symbols")
    private String name;

    public StyleDto(String name) {
        this.name = name;
    }

    public StyleDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
