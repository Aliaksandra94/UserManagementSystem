package com.moroz.testtask.test_task.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;
    @Column(name = "role_name")
    String name;
    @ManyToMany(mappedBy = "usersRole")
    Set<UserAccount> usersAccount;

    public Role() {
    }

    public Role(String name, Set<UserAccount> usersAccount) {
        this.name = name;
        this.usersAccount = usersAccount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserAccount> getUsersAccount() {
        return usersAccount;
    }

    public void setUsersAccount(Set<UserAccount> usersAccountList) {
        this.usersAccount = usersAccountList;
    }

    @Override
    public String getAuthority() {
        return getName();
    }

    @Override
    public String toString() {
        return name.toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id &&
                Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
