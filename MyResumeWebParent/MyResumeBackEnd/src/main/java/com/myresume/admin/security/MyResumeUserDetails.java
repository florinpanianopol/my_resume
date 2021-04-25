package com.myresume.admin.security;

import com.myresume.common.entity.Role;
import com.myresume.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;



public class MyResumeUserDetails implements UserDetails {


    private User user;


    public MyResumeUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for(Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return user.isEnabled();
    }

    public String getFullname(){
        return this.user.getFirstName()+" "+this.user.getLastName();
    }

    public Integer getId(){
        return this.user.getId();
    }


    public String getPhotosImagePath() {
        if(this.user.getId()==null||this.user.getPhotos() ==null) return "/images/pic.png";

        return "/user-photos/"+this.user.getId() +"/" +this.user.getPhotos();
    }

    public void setFirstName(String firstName) {
        this.user.setFirstName(firstName);
    }
    public void setLastName(String lastName) {
        this.user.setLastName(lastName);
    }

    public void setPhotos(String photos) {
        this.user.setPhotos(photos);
    }

}

