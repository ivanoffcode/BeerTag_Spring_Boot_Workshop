package com.example.beertag.mappers;

import com.example.beertag.entities.Role;
import com.example.beertag.entities.User;
import com.example.beertag.entities.dtos.RegisterDto;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {

    public User registerDtoToUser(RegisterDto registerDto){
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastname());

        Role role = new Role();

        role.setName("User");
        user.setRoles(Set.of(role));

        return user;
    }

}
