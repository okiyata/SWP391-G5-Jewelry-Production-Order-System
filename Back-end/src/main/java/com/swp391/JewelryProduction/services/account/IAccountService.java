package com.swp391.JewelryProduction.services.account;

import com.swp391.JewelryProduction.dto.AccountDTO;

import java.util.List;

public interface IAccountService {
    List<AccountDTO> findAllAccounts();
}
