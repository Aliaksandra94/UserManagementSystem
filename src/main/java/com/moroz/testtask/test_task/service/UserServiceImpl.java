package com.moroz.testtask.test_task.service;

import com.moroz.testtask.test_task.model.Role;
import com.moroz.testtask.test_task.model.Status;
import com.moroz.testtask.test_task.model.UserAccount;
import com.moroz.testtask.test_task.repository.RoleDao;
import com.moroz.testtask.test_task.repository.UserDao;
import com.moroz.testtask.test_task.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private RoleDao roleDao;
    private PasswordEncoder passwordEncoder;
    private MessageSource messageSource;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public UserAccount returnUserAccountByID(long id) {
        return userDao.getById(id);
    }

    @Override
    public void saveNewUserAccount(UserAccount userAccount, long roleID, String status) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleDao.getById(roleID));
        userAccount.setUsersRole(roles);
        userAccount.setStatus(Status.valueOf(status));
        userAccount.setCreatedAt(LocalDate.now());
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userDao.save(userAccount);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userDao.findByUsername(username);
        if (userAccount == null) {
            throw new UsernameNotFoundException(messageSource
                    .getMessage("error.authorization", new Object[]{"error.authorization"},
                            LocaleContextHolder.getLocale()));
        }
        if (!userAccount.isAccountNonLocked()) {
            throw new LockedException(messageSource
                    .getMessage("allPages.language.ru", new Object[]{"allPages.language.ru"},
                            LocaleContextHolder.getLocale()));
        }
        return UserAccount.fromUserAccount(userAccount);
    }


    @Override
    public Page<UserAccount> returnAllUserAccountList(Pageable page) {
        return userDao.findAll(page);
    }


    @Override
    public UserAccount returnUserAccountByUserName(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Page<UserAccount> findByPartOfUsername(String partOfUsername, Pageable pageable) {
        return userDao.findByPartOfUsername(partOfUsername, pageable);
    }

    @Override
    public boolean isUsernameAlreadyInUse(String username) {
        boolean userInDb = true;
        if (userDao.findByUsername(username) == null) userInDb = false;
        return userInDb;
    }

    @Override
    public void updateUserAccount(UserAccount userAccount, long roleID, String status) {
        UserAccount userToUpdate = userDao.getById(userAccount.getId());
        userToUpdate.setUsername(userAccount.getUsername());
        if (userToUpdate.getPassword().equals(userAccount.getPassword())) {
            userToUpdate.setPassword(userAccount.getPassword());
        } else {
            userToUpdate.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        }
        userToUpdate.setFirstName(userAccount.getFirstName());
        userToUpdate.setLastName(userAccount.getLastName());
        Set<Role> roles = new HashSet<>();
        roles.add(roleDao.getById(roleID));
        userToUpdate.setUsersRole(roles);
        userToUpdate.setStatus(Status.valueOf(status));
        userDao.save(userToUpdate);
    }

    @Override
    public void updateStatus(long id, String status) {
        UserAccount userAccount = userDao.getById(id);
        userAccount.setStatus(Status.valueOf(status));
        userDao.save(userAccount);
    }

    @Override
    public Page<UserAccount> returnUsersByRoleId(long id, Pageable pageable) {
        return userDao.findByRoleId(id, pageable);
    }
}
