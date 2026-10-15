package com.skylinetransit.controller;

import com.skylinetransit.auth.AuthSessionService;
import com.skylinetransit.model.UserAccount;
import com.skylinetransit.repository.UserAccountRepository;
import com.skylinetransit.repository.BusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserAccountRepository users;
    private final AuthSessionService sessions;
    private final BusRepository buses;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public AuthController(UserAccountRepository users, AuthSessionService sessions, BusRepository buses) { this.users=users; this.sessions=sessions; this.buses=buses; }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email=body.getOrDefault("email", "").trim().toLowerCase();
        String password=body.getOrDefault("password", "");
        String requestedRole=body.getOrDefault("role", "student").toLowerCase();
        UserAccount user=users.findByEmailIgnoreCase(email).orElse(null);
        if (user==null || !encoder.matches(password,user.getPasswordHash()) || !user.getRole().equals(requestedRole)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email, password, or account type."));
        return ResponseEntity.ok(Map.of("name",user.getName(),"role",user.getRole(),"email",user.getEmail(),"assignedBusId",user.getAssignedBusId()==null?"":user.getAssignedBusId(),"token",sessions.create(user)));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String,String> body) {
        String name=body.getOrDefault("name", "").trim(); String email=body.getOrDefault("email", "").trim().toLowerCase(); String password=body.getOrDefault("password", "");
        String busId=body.getOrDefault("busNumber", "").trim().toUpperCase();
        if(name.length()<2 || !email.matches("^\\S+@\\S+\\.\\S+$") || password.length()<8) return ResponseEntity.badRequest().body(Map.of("message","Enter a valid name, email, and password of at least 8 characters."));
        if(users.existsByEmailIgnoreCase(email)) return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","An account with this email already exists."));
        if (!busId.isBlank() && !buses.existsById(busId)) {
            var busByPlate = buses.findByPlateIgnoreCase(busId).orElse(null);
            if (busByPlate == null) return ResponseEntity.badRequest().body(Map.of("message", "Bus number not found. Use a bus ID such as B001, B006, B007, or the vehicle registration."));
            busId = busByPlate.getId();
        }
        UserAccount user=users.save(new UserAccount(name,email,encoder.encode(password),"student",busId.isBlank()?null:busId));
        return ResponseEntity.ok(Map.of("name",user.getName(),"role",user.getRole(),"email",user.getEmail(),"assignedBusId",user.getAssignedBusId()==null?"":user.getAssignedBusId(),"token",sessions.create(user)));
    }
}
