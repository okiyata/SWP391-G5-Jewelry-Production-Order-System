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
import com.swp391.JewelryProduction.util.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserInfoRepository infoRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;

    //<editor-fold desc="GET METHODS" defaultstate="collapsed">
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account findAccountByEmail(String email) {
        return accountRepository
                .findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Account with email " + email + " not found"));
    }

    @Override
    public Account findAccountById(String accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new ObjectNotFoundException("Account with id " + accountId + " does not exist"));
    }

    @Override
    public Account findAccountByEmailAndPassword(String email, String password) {
        Account acc = accountRepository.findByEmail(email).orElse(null);
        if (acc == null || !passwordEncoder.matches(password, acc.getPassword()))
            return null;

        return acc;
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

    @Override
    public Staff findStaffByRoleAndWorkStatus(Role role, WorkStatus workStatus) {
        return accountRepository.findStaffByRoleAndWorkStatus(role, workStatus).isPresent() ? accountRepository.findStaffByRoleAndWorkStatus(role, workStatus).get() : null;
    }

    @Override
    public List<Account> findAllByRole(Role role) {
        return accountRepository.findAllByRole(role);
    }
    //</editor-fold>

    //<editor-fold desc="UPDATE METHODS" defaultstate="collapsed">
    @Transactional
    @Override
    public Optional<Account> updateAccount(AccountDTO accountDTO) {
        Account updatedAcc = accountRepository.findByEmail(accountDTO.getEmail()).orElseThrow(() -> new ObjectNotFoundException("Account with email "+accountDTO.getEmail()+" does not exist"));
        updatedAcc.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        return accountRepository.save(updatedAcc);
    }

    @Transactional
    @Override
    public Optional<Account> updateAccountStatusActive(String email) {
        Account acc = accountRepository.findByEmail(email).orElse(null);
        if (acc == null) return null;
        acc.setStatus(AccountStatus.ACTIVE);
        return accountRepository.save(acc);
    }


    //</editor-fold>

    //<editor-fold desc="SAVE METHODS" defaultstate="collapsed">
    @Transactional
    @Override
    public void saveAccountPassword(AccountDTO accountDTO, String newPassword) {
        accountDTO.setPassword(newPassword);
        accountRepository.save(modelMapper.map(accountDTO, Account.class));
    }

    @Transactional
    @Override
    public Account saveAccountIfNew(AccountDTO accountDTO) {
        Account acc = accountRepository.findByEmail(accountDTO.getEmail()).orElse(null);
        if (acc != null && acc.getStatus().equals(AccountStatus.LOCKED)) {
            if (passwordEncoder.matches(accountDTO.getPassword(), acc.getPassword()))
                return acc;
        } else if (acc != null)
            return null;


        UserInfo info = new UserInfo();
        acc = Account.builder()
                .email(accountDTO.getEmail())
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .role(Role.CUSTOMER)
                .status(AccountStatus.LOCKED)
                .dateCreated(LocalDateTime.now())
                .userInfo(info)
                .build();
        info.setAccount(acc);

        accountRepository.save(acc);
        return acc;
    }

    @Transactional
    @Override
    public Account saveUserInfo(UserInfo info, String email) {
        Account acc = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Account with email " + email + " cannot be found, cannot update info"));
        info.setAccount(acc);
        userInfoRepository.save(info);
        return null;
    }
    //</editor-fold>

    //<editor-fold desc="DELETE METHODS" defaultstate="collapsed">
    @Transactional
    @Override
    public void deleteAccount(String accountID) {
        Account acc = accountRepository.findById(accountID).orElseThrow(() -> new ObjectNotFoundException("Account with id " + accountID + " not found, can't be deleted"));
        accountRepository.delete(acc);
    }
    //</editor-fold>


}
