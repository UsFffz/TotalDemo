package com.example.totaldemo.secrity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AdminDetails extends User {

    private Long id;


    public AdminDetails(String username, String password,Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AdminDetails{" +
                "id=" + id +
                ", " + super.toString() +
                '}';
    }
}
