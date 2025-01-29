package com.example.beertag.services;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;
import com.example.beertag.repositories.BeerRepository;
import com.example.beertag.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BeerRepository beerRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BeerRepository beerRepository) {
        this.userRepository = userRepository;
        this.beerRepository = beerRepository;
    }

    @Override
    public List<User> get() {
        return userRepository.get();
    }

    @Override
    public User get(int id) {
        return userRepository.get(id);
    }

    @Override
    public User get(String username) {
        return userRepository.get(username);
    }

    @Override
    public void addBeerToWishList(int beerId, int userId) {

        Beer beerToAddToWishlist = beerRepository.get(beerId);
        User userToAddToWishlist = userRepository.get(userId);

        userToAddToWishlist.getWishlist().add(beerToAddToWishlist);
        userRepository.update(userToAddToWishlist);
        userRepository.get(userId);
    }

    @Override
    public void removeFromWishList(int beerId, int userId) {
        Beer beerToAddToWishlist = beerRepository.get(beerId);
        User userToAddToWishlist = userRepository.get(userId);

        userToAddToWishlist.getWishlist().remove(beerToAddToWishlist);
        userRepository.update(userToAddToWishlist);
        userRepository.get(userId);
    }
}
