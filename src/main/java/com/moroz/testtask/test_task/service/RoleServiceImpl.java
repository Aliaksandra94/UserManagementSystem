package com.moroz.testtask.test_task.service;

import com.moroz.testtask.test_task.model.Role;
import com.moroz.testtask.test_task.repository.RoleDao;
import com.moroz.testtask.test_task.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{
    private RoleDao roleDao;
@Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Set<Role> returnAllRoles() {
        return new HashSet<>(roleDao.findAll());
    }
}
