package com.example.userservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@AllArgsConstructor
@ToString
public class User  implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "user_sequence",
            strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "username", nullable = false,unique = true, columnDefinition = "TEXT")
    private String username;

    @Column(name = "password", nullable = false ,columnDefinition = "TEXT")
    private String password;

    @Column(name = "email", nullable = false,unique = true, columnDefinition = "TEXT")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false )
    private UserRole role;

    @Column(name = "firstName", nullable = false, columnDefinition = "TEXT")
    private String FirstName;

    @Column(name = "lastName", nullable = false, columnDefinition = "TEXT")
    private String LastName;

    public User() {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getGrantedAuthorities();
//        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
