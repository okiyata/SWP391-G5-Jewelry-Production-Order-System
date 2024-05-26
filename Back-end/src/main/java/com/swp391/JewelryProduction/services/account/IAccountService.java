package com.swp391.JewelryProduction.services.account;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.pojos.Account;

import java.util.List;

public interface IAccountService {
    List<AccountDTO> findAllAccounts();
    Account saveAccount(Account account);
    AccountDTO findAccountById(String accountId);
    void updateAccount(AccountDTO accountDTO);
}
