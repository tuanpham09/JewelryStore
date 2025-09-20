package org.example.trangsuc_js.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Boolean enabled = true;
    
    @Column(name = "email_verified")
    private Boolean emailVerified = false;
    
    @Column(name = "phone_verified")
    private Boolean phoneVerified = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();
    
    // Bổ sung các trường cần thiết
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "province")
    private String province;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    @Column(name = "country")
    private String country = "Vietnam";
    
    // Preferences
    @Column(name = "preferred_language")
    private String preferredLanguage = "vi";
    
    @Column(name = "preferred_currency")
    private String preferredCurrency = "VND";
    
    // Relationships
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    
    // Helper methods
    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }
    
    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }
    
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }
    
    // Enum cho Gender
    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
