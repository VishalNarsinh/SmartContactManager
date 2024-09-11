package com.scm.entities;

import com.scm.helper.Providers;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @Column(length = 1000)
    private String about;

    @Column(length = 1000)
    private String imagePublicId;
    private String profilePictureURL;
    private String phoneNumber;

    private boolean enabled = false;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    private String emailToken;

    // SELF,GOOGLE,FACEBOOK,GITHUB,LINKEDIN
    @Enumerated(EnumType.STRING)
    private Providers provider = Providers.SELF;
    private String providerUserId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorityList = roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
        return this.enabled;
    }
}
