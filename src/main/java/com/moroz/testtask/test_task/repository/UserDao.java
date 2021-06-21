package com.moroz.testtask.test_task.repository;

import com.moroz.testtask.test_task.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;


@Repository
public interface UserDao extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsername(String username);

    @Query(value = "from UserAccount userAccount where lower(userAccount.username) like lower(concat('%',:partOfUsername,'%'))")
    Page<UserAccount> findByPartOfUsername(String partOfUsername, Pageable pageable);
    @Query(value = "select from users_account where username ilike concat('%',:partOfUsername,'%')", nativeQuery = true)
    Page<UserAccount> findByPartOfUsernameIgnoreCase(String partOfUsername, Pageable pageable);
    @Query(value = "select r.usersAccount from Role r where r.id =:id")
    Page<UserAccount> findByRoleId(long id, Pageable pageable);
}
