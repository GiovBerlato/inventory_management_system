package io.github.giovberlato.inventory_management_system;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementSystemApplication.class, args);
	}



	// Seed a test user for authentication purposes (DELETE LATER)
	@Bean
	org.springframework.boot.CommandLineRunner seedUser(
    io.github.giovberlato.inventory_management_system.repository.UserRepository repository,
    org.springframework.security.crypto.password.PasswordEncoder encoder) 
	{
		return args -> {
			if (repository.findByUsername("giovanni").isEmpty()) {
				var user = new io.github.giovberlato.inventory_management_system.model.User(
					"giovanni",
					encoder.encode("secret123"),
					io.github.giovberlato.inventory_management_system.security.Role.ROLE_ADMIN
				);
				repository.save(user);
				System.out.println(">>> Test user 'giovanni' created successfully!");
			}
		};
	}

}
