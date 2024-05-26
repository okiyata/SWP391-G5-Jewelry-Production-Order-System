package com.swp391.JewleryProduction.services.account;

import com.swp391.JewleryProduction.dto.AccountDTO;
import com.swp391.JewleryProduction.pojos.Account;

import java.util.List;

public interface IAccountService {
    List<AccountDTO> findAllAccounts();
    void saveAccount(AccountDTO accountDTO);
    AccountDTO findAccountById(String accountId);
    AccountDTO findAccountByEmailAndPassword(String email, String password);
    void updateAccount(AccountDTO accountDTO);
    boolean findAccountByEmail(String email);

    void saveAccountPassword(AccountDTO accountDTO, String newPassword);
}
