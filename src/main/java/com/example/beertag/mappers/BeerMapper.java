package com.example.beertag.mappers;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;
import com.example.beertag.entities.dtos.BeerDto;
import com.example.beertag.services.BeerService;
import com.example.beertag.services.BreweryService;
import com.example.beertag.services.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeerMapper {

    private BeerService beerService;

    private StyleService styleService;

    private BreweryService breweryService;

    @Autowired
    public BeerMapper(BeerService beerService, StyleService styleService, BreweryService breweryService) {
        this.beerService = beerService;
        this.styleService = styleService;
        this.breweryService = breweryService;
    }

    public Beer fromDto(int id, BeerDto dto) {
        Beer beer = fromDto(dto);
        beer.setId(id);
        Beer repositoryBeer = beerService.get(id);
        beer.setCreatedBy(repositoryBeer.getCreatedBy());
        return beer;
    }
    public Beer fromDto(BeerDto beerDto){
        Beer beer = new Beer();
        beer.setName(beerDto.getName());
        beer.setDescription(beerDto.getDescription());
        beer.setAbv(beerDto.getAbv());
        beer.setStyle(styleService.get(beerDto.getStyleId()));
        beer.setBrewery(breweryService.getByID(beerDto.getBreweryId()));
        return beer;
    }

    public Beer fromDto(BeerDto beerDto, User creator){
        Beer beer = new Beer();
        beer.setName(beerDto.getName());
        beer.setDescription(beerDto.getDescription());
        beer.setAbv(beerDto.getAbv());
        beer.setStyle(styleService.get(beerDto.getStyleId()));
        beer.setBrewery(breweryService.getByID(beerDto.getBreweryId()));
        beer.setCreatedBy(creator);
        return beer;
    }

    public BeerDto toDto(Beer beer) {
        BeerDto beerDto = new BeerDto();
        beerDto.setName(beer.getName());
        beerDto.setDescription(beer.getDescription());
        beerDto.setAbv(beer.getAbv());
        beerDto.setStyleId(beer.getStyle().getId());
        beerDto.setBreweryId(beer.getBrewery().getId());
        return beerDto;
    }

}
