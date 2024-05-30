package com.swp391.JewelryProduction.repositories;

import com.swp391.JewelryProduction.pojos.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
//    Account findAccountByEmailAndPassword(String email, String password);
//    boolean findAccountByEmail(String email);
    Optional<Account> findByEmail (String email);
}
