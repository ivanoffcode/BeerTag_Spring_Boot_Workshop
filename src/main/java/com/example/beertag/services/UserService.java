package com.example.beertag.services;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getById(int id);

    User getByUsername(String username);

    void addBeerToWishList(int beerId, int userId);

    void removeFromWishList(int beerId, int userId);

    void create(User user);

    void update(User user);

    void delete(int id);

}
