package com.example.beertag.repositories;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers();

    User getById(int id);

    User getByUsername(String username);

    void update(User user);

    void create(User beer);

    void delete(int id);

}
