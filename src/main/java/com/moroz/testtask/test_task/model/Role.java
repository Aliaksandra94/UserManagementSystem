package com.moroz.testtask.test_task.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;
    @Column(name = "role_name")
    String name;
    @ManyToMany(mappedBy = "usersRoleList")
    Set<UserAccount> usersAccountList;

    public Role() {
    }

    public Role(String name, Set<UserAccount> usersAccountList) {
        this.name = name;
        this.usersAccountList = usersAccountList;
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

    public Set<UserAccount> getUsersAccountList() {
        return usersAccountList;
    }

    public void setUsersAccountList(Set<UserAccount> usersAccountList) {
        this.usersAccountList = usersAccountList;
    }

    @Override
    public String getAuthority() {
        return getName();
    }

    @Override
    public String toString() {
        return name.toLowerCase();
    }
}
