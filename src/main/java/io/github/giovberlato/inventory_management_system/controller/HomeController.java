package io.github.giovberlato.inventory_management_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String welcomeMessage() {
        return "Welcome to the Inventory Management System!\nRead the README.md or the GitHub page to figure out how to use this API.";
    }
}
