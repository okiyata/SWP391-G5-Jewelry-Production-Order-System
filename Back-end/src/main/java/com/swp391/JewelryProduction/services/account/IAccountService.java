package com.swp391.JewelryProduction.services.account;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.pojos.Account;

import java.util.List;

public interface IAccountService {
    List<AccountDTO> findAllAccounts();
    AccountDTO saveAccount(AccountDTO accountDTO);
    AccountDTO findAccountById(String accountId);
    AccountDTO findAccountByEmailAndPassword(String email, String password);
    void updateAccount(AccountDTO accountDTO);
    AccountDTO findAccountByEmail(String email);
    AccountDTO updateAccountStatusActive (String email);

    void saveAccountPassword(AccountDTO accountDTO, String newPassword);
}
