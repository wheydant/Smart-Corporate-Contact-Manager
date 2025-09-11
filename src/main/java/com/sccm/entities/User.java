package com.sccm.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.relation.RoleList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
//For representing User Details spring security needs a UserDetail object so we can just extend User to UserDetail so we can user User wherever we want to fetch userDetails
public class User implements UserDetails{
    
    @Id
    private String userId;

    @Column(name="user_name", nullable=false)
    private String name;
    private String organization;

    @Column(unique=true, nullable=false)
    private String email;
    //This is a way to skip lambook's getter setter
    @Getter(AccessLevel.NONE)
    private String password;

    @Column(length=1000)
    private String about;

    @Column(length=1000)
    private String profilePic;
    private String phoneNumber;
    private boolean enabled = false;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    //Login method
    @Enumerated(value = EnumType.STRING)
    private Providers provider = Providers.SELF;
    private String providerUserId;

    //ADD fields if needed.
    //Mapping
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    //UserDetails unimplemented methods
    //To fetch all the roles of the users, it is list as user can be admin/user/client
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    private String emailToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //We can return normal List of String we need Collection which extends GrantedAuthority 
        Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled(){
        return this.enabled;
    }
    
}
