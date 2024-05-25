package com.swp391.JewleryProduction.services.account;

import com.swp391.JewleryProduction.dto.AccountDTO;

import java.util.List;

public interface IAccountService {
    List<AccountDTO> findAllAccounts();
    Account saveAccount(Account account);
    AccountDTO findAccountById(String accountId);
    void updateAccount(AccountDTO accountDTO);
}
