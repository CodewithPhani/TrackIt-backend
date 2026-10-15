package com.skylinetransit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_accounts", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120) private String name;
    @Column(nullable = false, unique = true, length = 180) private String email;
    @Column(nullable = false, length = 100) private String passwordHash;
    @Column(nullable = false, length = 20) private String role;
    @Column(length = 20) private String assignedBusId;
    public UserAccount() {}
    public UserAccount(String name, String email, String passwordHash, String role, String assignedBusId) { this.name=name; this.email=email.toLowerCase(); this.passwordHash=passwordHash; this.role=role; this.assignedBusId=assignedBusId; }
    public Long getId(){return id;} public String getName(){return name;} public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;} public String getRole(){return role;} public String getAssignedBusId(){return assignedBusId;}
    public void setId(Long id){this.id=id;} public void setName(String name){this.name=name;} public void setEmail(String email){this.email=email.toLowerCase();} public void setPasswordHash(String passwordHash){this.passwordHash=passwordHash;} public void setRole(String role){this.role=role;} public void setAssignedBusId(String assignedBusId){this.assignedBusId=assignedBusId;}
}
