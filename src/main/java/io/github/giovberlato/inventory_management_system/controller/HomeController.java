package io.github.giovberlato.inventory_management_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Operation(
            summary = "API welcome page",
            description = "Returns a short welcome message with basic guidance for using the inventory management API."
    )
    @GetMapping("/")
    public String welcomeMessage() {
        return "Welcome to the Inventory Management System!\nRead the README.md or the GitHub page to figure out how to use this API.";
    }
}
