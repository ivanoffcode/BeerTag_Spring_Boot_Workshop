package com.example.beertag.entities.dtos;

import com.example.beertag.entities.Beer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotNull(message = "Username can't be empty")
    @Size(min = 2, max = 20, message = "Username should be between 2 and 20 symbols")
    private String username;

    @NotNull(message = "Password can't be empty")
    @Size(min = 2, max = 50, message = "Password should be between 2 and 50 symbols")
    private String password;

    @NotNull(message = "Email can't be empty")
    @Size(min = 2, max = 20, message = "Email should be between 2 and 20 symbols")
    private String email;

    @NotNull(message = "Choose whether it is an admin or not.")
    private boolean isAdmin;

    public UserDto(String username, String password, String email, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public UserDto() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
