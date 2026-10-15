package com.skylinetransit.config;

import com.skylinetransit.model.UserAccount;
import com.skylinetransit.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthInitializer {
    @Bean CommandLineRunner seedAccounts(UserAccountRepository users) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            seed(users, encoder, "Transport Admin", "admin@anurag.edu.in", "Admin@123", "admin", null);
            seed(users, encoder, "College Driver 1", "driver@anurag.edu.in", "Driver@123", "driver", "B001");
            seed(users, encoder, "College Driver 6", "driver006@anurag.edu.in", "Driver006@123", "driver", "B006");
            seed(users, encoder, "College Driver 7", "driver007@anurag.edu.in", "Driver007@123", "driver", "B007");
        };
    }
    private void seed(UserAccountRepository users, BCryptPasswordEncoder encoder, String name, String email, String password, String role, String bus) {
        if (!users.existsByEmailIgnoreCase(email)) users.save(new UserAccount(name,email,encoder.encode(password),role,bus));
    }
}
