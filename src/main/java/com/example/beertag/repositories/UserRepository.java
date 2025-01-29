package com.example.beertag.repositories;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;

import java.util.List;

public interface UserRepository {

    List<User> get();

    User get(int id);

    User get(String username);

    void update(User user);

}
