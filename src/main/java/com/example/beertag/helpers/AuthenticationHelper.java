package com.example.beertag.helpers;

import com.example.beertag.entities.User;
import com.example.beertag.exceptions.AuthenticationFailureException;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.exceptions.UnauthorizedOperationException;
import com.example.beertag.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    public static final String AUTHENTICATION_FAILURE_MESSAGE = "Wrong username or password";
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String INVALID_AUTHENTICATION = "Invalid authentication";

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (headers == null || !headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION);
        }

        try {
            String credentials = headers.getFirst(AUTHORIZATION_HEADER_NAME);

            if (credentials == null || credentials.isBlank()) {
                throw new UnauthorizedOperationException(INVALID_AUTHENTICATION);
            }

            String[] userParameters = credentials.split(" ");
            if (userParameters.length != 2) {
                throw new UnauthorizedOperationException(INVALID_AUTHENTICATION);
            }

            String username = userParameters[0];
            String password = userParameters[1];
            User userToCheck = userService.getByUsername(username);

            if (userToCheck == null
                    || userToCheck.getPassword() == null
                    || !userToCheck.getPassword().equals(password)) {
                throw new UnauthorizedOperationException(INVALID_AUTHENTICATION);
            }
            return userToCheck;

        } catch (EntityNotFoundException e) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION);
        }
    }

    public User tryGetUser(HttpSession session){
        String currentUser = (String) session.getAttribute("currentUser");
        if (currentUser == null){
            throw new AuthenticationFailureException("No user logged in!");
        }
        return userService.getByUsername(currentUser);
    }

    public User verifyAuthentication(String username, String password){
        try{
            User user = userService.getByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
            }
            return user;
        } catch (EntityNotFoundException e){
            throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
        }
    }
}
