package com.moroz.testtask.test_task.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users_account")
public class UserAccount implements UserDetails {
    @Id
    @SequenceGenerator(name = "seq", sequenceName = "user_id_seq", allocationSize = 1, initialValue = 4)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private long id;
    @Column(name = "username", unique = true, nullable = false, length = 16)
    private String username;
    @Column(name = "password", nullable = false, length = 2048)
    private String password;
    @Column(name = "first_name", nullable = false, length = 16)
    private String firstName;
    private String lastName;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> usersRole = new HashSet<>();
    private Status status;
    private LocalDate createdAt;

    public UserAccount() {
    }

    public UserAccount(@Pattern(regexp = "[a-zA-Z]{3,16}") String username,
                       @Size(min = 3, max = 16) @Pattern(regexp = "(?=(.*\\d){1})(?=(.*[a-zA-Z]){1}).[a-zA-Z0-9]{3,16}") String password,
                       @Size(min = 1, max = 16) @Pattern(regexp = "[a-zA-Z]{1,16}") String firstName,
                       @Size(min = 1, max = 16) @Pattern(regexp = "[a-zA-Z]{1,16}") String lastName,
                       Set<Role> usersRole,
                       Status status, LocalDate createdAt) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.usersRole = usersRole;
        this.status = status;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Role> getUsersRole() {
        return usersRole;
    }

    public void setUsersRole(Set<Role> usersRoleList) {
        this.usersRole = usersRoleList;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> list = new HashSet<>();
        for (Role role : usersRole) {
            list.add(new SimpleGrantedAuthority(role.getName()));
        }
        return list;
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
        if (getStatus().equals(Status.INACTIVE)){
            return false;
        }
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

    public static UserDetails fromUserAccount(UserAccount userAccount) {
        return new org.springframework.security.core.userdetails.User(
                userAccount.getUsername(), userAccount.getPassword(),
                userAccount.isEnabled(), userAccount.isAccountNonExpired(),
                userAccount.isCredentialsNonExpired(),
                userAccount.isAccountNonLocked(), userAccount.getAuthorities());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return id == that.id &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
