package com.example.beertag.entities.dtos;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class BeerDto {

    @NotNull(message = "Name can't be empty")
    @Size(min = 2, max = 20, message = "Name should be between 2 and 20 symbols")
    private String name;

    @NotNull(message = "Description can't be empty")
    @Size(min = 10, max = 500, message = "Description should be between 10 and 500 characters")
    private String description;

    @NotNull(message = "Style can't be empty")
    @Positive(message = "Style should be positive")
    private int styleId;

    @NotNull(message = "ABV can't be empty")
    @Positive(message = "ABV should be positive")
    private double abv;

    @NotNull(message = "Brewery can't be empty")
    private int breweryId;

    @NotNull(message = "Creator can't be empty")
    private int createdByUserId;

    public BeerDto(String name, String description, double abv, int styleId, int breweryId, int createdByUserId) {
        this.name = name;
        this.description = description;
        this.styleId = styleId;
        this.abv = abv;
        this.breweryId = breweryId;
        this.createdByUserId = createdByUserId;
    }

    public BeerDto(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public double getAbv() {
        return abv;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    public int getBreweryId() {
        return breweryId;
    }

    public void setBreweryId(int breweryId) {
        this.breweryId = breweryId;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }
}
