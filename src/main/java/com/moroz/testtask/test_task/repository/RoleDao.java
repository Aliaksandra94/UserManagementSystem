package com.moroz.testtask.test_task.repository;

import com.moroz.testtask.test_task.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleDao extends JpaRepository<Role, Long> {
}
