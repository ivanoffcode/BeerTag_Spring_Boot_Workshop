package com.example.beertag.controllers.mvc;

import com.example.beertag.services.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/wishlist")
public class WishlistMvcController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistMvcController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/add")
    public String addToWishlist(@ModelAttribute("beerId") int beerId, HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth/login"; // Redirect to login if not authenticated
        }
        wishlistService.addBeerToWishlist(currentUser, beerId);
        return "redirect:/beers"; // Redirect back to the beers page
    }

    @PostMapping("/mark-as-drunk")
    public String markAsDrunk(@ModelAttribute("beerId") int beerId, HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth/login"; // Redirect to login if not authenticated
        }
        wishlistService.markBeerAsDrunk(currentUser, beerId);
        return "redirect:/beers"; // Redirect back to the beers page
    }
}
