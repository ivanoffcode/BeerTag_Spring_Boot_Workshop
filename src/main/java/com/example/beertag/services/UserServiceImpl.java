package com.example.beertag.services;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.exceptions.EntityNotFoundException;
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
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public void addBeerToWishList(int beerId, int userId) {

        Beer beerToAddToWishlist = beerRepository.get(beerId);
        User userToAddToWishlist = userRepository.getById(userId);

        userToAddToWishlist.getWishlist().add(beerToAddToWishlist);
        userRepository.update(userToAddToWishlist);
        userRepository.getById(userId);
    }

    @Override
    public void removeFromWishList(int beerId, int userId) {
        Beer beerToAddToWishlist = beerRepository.get(beerId);
        User userToAddToWishlist = userRepository.getById(userId);

        userToAddToWishlist.getWishlist().remove(beerToAddToWishlist);
        userRepository.update(userToAddToWishlist);
        userRepository.getById(userId);
    }

    @Override
    public void create(User user) {

        boolean duplicateExists = true;
        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }

        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }
}
