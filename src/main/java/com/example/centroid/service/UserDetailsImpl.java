package com.example.centroid.service;

import com.example.centroid.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersion = 1L;

    private final long id;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    @JsonIgnore
    private final String password;

    public UserDetailsImpl(long id,String username, String email, String password,String firstName,String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName=firstName;
        this.lastName = lastName;
    }

    //todo associate the user with a active status like in google chat

    public static UserDetailsImpl build(User user){
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName()
        );
    }
    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEmail(){
        return email;
    }

    public Long getId(){
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; //for now lets return null and let's see what will be the output
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
