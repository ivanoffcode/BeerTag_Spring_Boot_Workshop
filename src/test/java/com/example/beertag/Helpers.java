package com.example.beertag;

import com.example.beertag.entities.*;
import com.example.beertag.entities.dtos.BeerDto;
import com.example.beertag.entities.filter.FilterOptions;

public class Helpers {

    public static User createMockAdminUser() {

        User mockAdmin = createMockNormalUser();
        mockAdmin.setAdmin(true);

        return mockAdmin;
    }
    public static User createMockNormalUser() {

        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("mock@user.com");
        mockUser.setUsername("MockUser");
        mockUser.setPassword("MockPassword");
        mockUser.setAdmin(false);
        return mockUser;
    }

    public static Beer createMockBeer() {

        Beer mockBeer = new Beer();
        mockBeer.setId(1);
        mockBeer.setName("TestBeer");
        mockBeer.setDescription("TestDescription");
        mockBeer.setAbv(5.5);
        mockBeer.setStyle(createMockStyle());
        mockBeer.setBrewery(createMockBrewery());
        mockBeer.setCreatedBy(createMockAdminUser());
        return mockBeer;
    }

    public static BeerDto createMockBeerDto() {
        return new BeerDto("TestBeer","TestDescription",  5.5, 1, 1, 1);
    }

    public static Beer createMockBeerFromBeerDto() {
        Beer beer = new Beer();
        beer.setName("TestBeer");
        beer.setDescription("TestDescription");
        beer.setAbv(5.5);
        beer.setStyle(createMockStyle());
        beer.setBrewery(createMockBrewery());
        return beer;
    }

    private static Brewery createMockBrewery() {

        Brewery mockBrewery = new Brewery();
        mockBrewery.setId(1);
        mockBrewery.setName("TestBrewery");
        mockBrewery.setCountry(createMockCountry());
        return mockBrewery;
    }

    private static Country createMockCountry() {

        Country mockCountry = new Country();
        mockCountry.setId(1);
        mockCountry.setName("MockCountry");
        return mockCountry;
    }

    public static FilterOptions createMockFilterOptions() {
        return new FilterOptions("name", 0.0, 10.0, 1, "sort", "order");
    }

    private static Style createMockStyle() {

        Style mockStyle = new Style();
        mockStyle.setId(1);
        mockStyle.setName("MockStyle");
        return mockStyle;

    }


}
