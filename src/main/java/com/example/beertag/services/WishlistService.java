package com.example.beertag.services;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.repositories.BeerRepository;
import com.example.beertag.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final UserRepository userRepository;
    private final BeerRepository beerRepository;

    @Autowired
    public WishlistService(UserRepository userRepository, BeerRepository beerRepository) {
        this.userRepository = userRepository;
        this.beerRepository = beerRepository;
    }

    public void addBeerToWishlist(String username, int beerId) {
        User user = userRepository.getByUsername(username);
        Beer beer = beerRepository.get(beerId);

        user.getWishlist().add(beer); // Assuming User has a `Set<Beer>` for wishlist
        userRepository.update(user);
    }

    public void markBeerAsDrunk(String username, int beerId) {
        User user = userRepository.getByUsername(username);
        Beer beer = beerRepository.get(beerId);

        user.getDrunkBeers().add(beer); // Assuming User has a `Set<Beer>` for drunk beers
        userRepository.update(user);
    }
}
