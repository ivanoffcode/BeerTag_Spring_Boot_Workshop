package com.example.beertag.controllers.mvc;

import com.example.beertag.entities.User;
import com.example.beertag.exceptions.AuthenticationFailureException;
import com.example.beertag.helpers.AuthenticationHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final AuthenticationHelper authenticationHelper;

    public HomeMvcController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session){
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage() {
        return "HomeView";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "AboutView";
    }

    @GetMapping("/admin")
    public String showAdminPortalView(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (!user.isAdmin()) {
                model.addAttribute("status", HttpStatus.NOT_FOUND.value()
                        + " " + HttpStatus.NOT_FOUND.getReasonPhrase());
                model.addAttribute("error", "You are not allowed to view this page!");
                return "ErrorView";
            }
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }
        return "AdminView";
    }
}
