package com.example.beertag.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "brewery")
public class Brewery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message = "Name can't be empty")
    @Size(min = 2, max = 20, message = "Name should be between 2 and 20 symbols")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Country can't be empty")
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    //@OneToMany(mappedBy = "brewery")
    //private List<Beer> beers = new ArrayList<>();

    public Brewery() {
    }

    public Brewery(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brewery brewery = (Brewery) o;
        return id == brewery.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
