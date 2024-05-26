package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.services.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {
    private AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping
    public String getRegisterForm(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "register-page";
    }

    @PostMapping
    public String saveAccount(@ModelAttribute("account") Account account) {
        accountService.saveAccount(account);
        return "redirect:/login-page";
    }

    @GetMapping("/.../{accountId}/edit")
    public String getEditForm(@PathVariable("accountId") String accountId, Model model) {
        AccountDTO account = accountService.findAccountById(accountId);
        model.addAttribute("account", account);
        return "edit-page";
    }

    @PostMapping("/.../{accountId}/edit")
    public String updateAccount(@ModelAttribute("account") AccountDTO accountDTO) {
        accountService.updateAccount(accountDTO);
        return "redirect:/home-page";
    }
}
