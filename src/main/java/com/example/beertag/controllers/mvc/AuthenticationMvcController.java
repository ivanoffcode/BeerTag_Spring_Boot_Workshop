package com.example.beertag.controllers.mvc;

import com.example.beertag.entities.User;
import com.example.beertag.entities.dtos.LoginDto;
import com.example.beertag.entities.dtos.RegisterDto;
import com.example.beertag.exceptions.AuthenticationFailureException;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.mappers.UserMapper;
import com.example.beertag.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public AuthenticationMvcController(AuthenticationHelper authenticationHelper, UserService userService, UserMapper userMapper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session){
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage (Model model) {
        model.addAttribute("register", new RegisterDto());
        return "RegisterView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto loginDto,
                              BindingResult bindingResult,
                              HttpSession session){
        if (bindingResult.hasErrors()){
            return "LoginView";
        }
        try {
           User user = authenticationHelper.verifyAuthentication(loginDto.getUsername(),
                    loginDto.getPassword());
            session.setAttribute("currentUser", loginDto.getUsername());
            session.setAttribute("isAdmin", user.isAdmin());
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "auth_error",
                    e.getMessage());
            return "LoginView";
        }
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto registerDto,
                                 BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "RegisterView";
        }

        if(!registerDto.getPassword().equals(registerDto.getPasswordConfirm())){
            bindingResult.rejectValue("passwordConfirm", "password_error",
                    "Password does not match!");
            return "RegisterView";
        }

        try{
            User user = userMapper.registerDtoToUser(registerDto);
            userService.create(user);
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "RegisterView";
        }
    }
}
