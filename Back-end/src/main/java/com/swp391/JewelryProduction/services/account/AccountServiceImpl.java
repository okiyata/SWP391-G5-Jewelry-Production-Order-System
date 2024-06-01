package com.swp391.JewelryProduction.services.account;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.enums.AccountStatus;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.enums.WorkStatus;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Staff;
import com.swp391.JewelryProduction.pojos.UserInfo;
import com.swp391.JewelryProduction.repositories.AccountRepository;
import com.swp391.JewelryProduction.repositories.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserInfoRepository infoRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<AccountDTO> findAllAccounts() {
        return accountRepository.findAll().stream().map(account -> mapToAccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public AccountDTO saveAccountIfNew(AccountDTO accountDTO) {
        if (accountRepository.existsByEmail(accountDTO.getEmail()))
            return null;

        accountDTO.setRole(Role.CUSTOMER);
        accountDTO.setStatus(AccountStatus.LOCKED);
        accountDTO.setDateCreated(LocalDateTime.now());
        accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
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
        Account acc = accountRepository.findByEmail(email).orElse(null);
        if (acc == null || !passwordEncoder.matches(password, acc.getPassword()))
            return null;

        return this.modelMapper.map(acc, AccountDTO.class);
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

    @Override
    public Staff findStaffByRoleAndWorkStatus(Role role, WorkStatus workStatus) {
        return accountRepository.findStaffByRoleAndWorkStatus(role, workStatus).isPresent() ? accountRepository.findStaffByRoleAndWorkStatus(role, workStatus).get() : null;
    }

    @Override
    public UserInfo findInfoById(String id) {
        return infoRepository.findById(id).orElse(null);
    }

    @Override
    public UserInfo findInfoByEmail(String email) {
        Account acc = accountRepository.findByEmail(email).orElse(null);
        if (acc == null) return null;
        return infoRepository.findById(acc.getId()).orElse(null);
    }

    @Override
    public Account findAccountByRole(Role role) {
        return accountRepository.findAccountByRole(role).isPresent() ?  accountRepository.findAccountByRole(role).get() : null;
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
