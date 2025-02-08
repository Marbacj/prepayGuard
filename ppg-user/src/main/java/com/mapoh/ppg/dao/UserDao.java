package com.mapoh.ppg.dao;

import com.mapoh.ppg.dto.UserInfoRequest;
import com.mapoh.ppg.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author mabohv
 * @date 2024/12/25 22:28
 */

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    List<User> findAllByUserNameAndIdCardNumber(String userName, String idCardNumber);

    boolean existsByUserNameAndPassword(String userName, String password);

    boolean existsByUserNameAndIdCardNumber(String userName, String idCardNumber);

    /**
     * updateUserInfo
     */
    @Modifying
    @Query("UPDATE User u SET u.password = :password, u.phoneNumber = :phoneNumber, " +
            "u.email = :email, u.accountName = :accountName WHERE u.id = :id")
    int modifyUserInfo(@Param("id") Long id,
                       @Param("accountName") String accountName,
                       @Param("email") String email,
                       @Param("password") String password,
                       @Param("phoneNumber") String phoneNumber);

    @Query("SELECT u.balance from User  u WHERE u.id = :userId")
    BigDecimal getBalance(@Param("userId") Long userId);


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.balance = :sub where u.id = :userId")
    void modifyUserBalance(@Param("userId") Long userId,@Param("sub") BigDecimal sub);
}
