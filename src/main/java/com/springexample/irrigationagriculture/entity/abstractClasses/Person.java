package com.springexample.irrigationagriculture.entity.abstractClasses;

import com.springexample.irrigationagriculture.entity.PlantHouse;
import com.springexample.irrigationagriculture.entity.enums.Role;
import com.springexample.irrigationagriculture.entity.enums.TimeZone;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @NonNull
    private String username;
    private String password;
    private Boolean command;
    private TimeZone timeZone;
    private TimeZone timeZone2;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private PlantHouse plantHouse;

    public Person(Boolean command, Long id, String name, String password, PlantHouse plantHouse, Role role, TimeZone timeZone2, TimeZone timeZone, @NonNull String username) {
        this.command = command;
        this.id = id;
        this.name = name;
        this.password = password;
        this.plantHouse = plantHouse;
        this.role = role;
        this.timeZone2 = timeZone2;
        this.timeZone = timeZone;
        this.username = username;
    }

    public Person() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getCommand() {
        return command;
    }
    public void setCommand(Boolean command) {
        this.command = command;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlantHouse getPlantHouse() {
        return plantHouse;
    }

    public void setPlantHouse(PlantHouse plantHouse) {
        this.plantHouse = plantHouse;
    }

    public TimeZone getTimeZone2() {
        return timeZone2;
    }

    public void setTimeZone2(TimeZone timeZone2) {
        this.timeZone2 = timeZone2;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
