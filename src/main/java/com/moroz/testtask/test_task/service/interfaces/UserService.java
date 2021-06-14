package com.moroz.testtask.test_task.service.interfaces;

import com.moroz.testtask.test_task.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestParam;


public interface UserService extends UserDetailsService {
    Page<UserAccount> returnAllUserAccountList(Pageable page);
    UserAccount returnUserAccountByUserName(String username);
    UserAccount returnUserAccountByID(long id);
    void saveNewUserAccount(UserAccount userAccount, long roleID, String status);
    Page<UserAccount> findByPartOfUsername(String partOfUsername, Pageable pageable);
    boolean isUsernameAlreadyInUse(String username);
    void updateUserAccount(UserAccount userAccount, long roleID, String status);
    void updateStatus(long id, String status);
    Page<UserAccount> returnUsersByRoleId(long id, Pageable pageable);


}
