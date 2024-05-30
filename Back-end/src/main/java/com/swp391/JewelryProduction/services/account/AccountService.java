package com.swp391.JewelryProduction.services.account;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.enums.AccountStatus;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService{

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<AccountDTO> findAllAccounts() {
        return accountRepository.findAll().stream().map(account -> mapToAccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public AccountDTO saveAccount(AccountDTO accountDTO) {
        accountDTO.setRole(Role.CUSTOMER);
        accountDTO.setStatus(AccountStatus.LOCKED);
        accountDTO.setDateCreated(LocalDateTime.now());
        Account accModel = modelMapper.map(accountDTO, Account.class);
        accountRepository.save(accModel);
        return accountDTO;
    }

    @Override
    public AccountDTO findAccountById(String accountId) {
        return mapToAccountDTO(accountRepository.findById(accountId).get());
    }

    @Override
    public AccountDTO findAccountByEmailAndPassword(String email, String password) {
//        return mapToAccountDTO(accountRepository.findAccountByEmailAndPassword(email, password))
        return null;
    }

    @Override
    public void updateAccount(AccountDTO accountDTO) {
        accountRepository.save(mapToAccount(accountDTO));
    }

    @Override
    public AccountDTO findAccountByEmail(String email) {
        return modelMapper.map(accountRepository.findByEmail(email), AccountDTO.class);
    }

    @Override
    public AccountDTO updateAccountStatusActive(String email) {
        Account acc = accountRepository.findByEmail(email).orElse(null);
        if (acc == null) return null;
        acc.setStatus(AccountStatus.ACTIVE);
        return modelMapper.map(accountRepository.save(acc), AccountDTO.class);
    }

    @Override
    public void saveAccountPassword(AccountDTO accountDTO, String newPassword) {
        accountDTO.setPassword(newPassword);
        accountRepository.save(mapToAccount(accountDTO));
    }

    private Account mapToAccount(AccountDTO accountDTO) {
        return Account.builder()
                .id(accountDTO.getId())
                .email(accountDTO.getEmail())
                .password(accountDTO.getPassword())
                .dateCreated(accountDTO.getDateCreated())
                .role(accountDTO.getRole())
                .status(accountDTO.getStatus())
                .build();
    }

    private AccountDTO mapToAccountDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .email(account.getEmail())
                .password(account.getPassword())
                .dateCreated(account.getDateCreated())
                .role(account.getRole())
                .status(account.getStatus())
                .build();
    }
}
