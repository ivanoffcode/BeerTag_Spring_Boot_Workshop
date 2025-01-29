package com.example.beertag.entities.dtos;

import com.example.beertag.entities.Brewery;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BreweryDto {

    @NotNull(message = "Name can't be empty")
    @Size(min = 2, max = 20, message = "Name should be between 2 and 20 symbols")
    private String name;

    @NotNull(message = "Country can't be empty")
    private int countryId;


    public BreweryDto(String name, int countryId) {
        this.name = name;
        this.countryId = countryId;
    }

    public BreweryDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
