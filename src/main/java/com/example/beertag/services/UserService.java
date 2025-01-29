package com.example.beertag.services;

import com.example.beertag.entities.User;

import java.util.List;

public interface UserService {

    List<User> get();

    User get(int id);

    User get(String username);

    void addBeerToWishList(int beerId, int userId);

    void removeFromWishList(int beerId, int userId);

}
