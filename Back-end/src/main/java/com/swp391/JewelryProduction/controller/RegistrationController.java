package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.security.services.AuthenticationService;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.util.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registration")
public class RegistrationController {
    private final static Logger logger = LoggerFactory.getLogger(RegistrationController.class);


    private final AccountService accountService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(
            @Valid @RequestBody AccountDTO accountDTO,
            BindingResult bindingResult
    ) {
        ResponseEntity<Response> errorMsg = getResponseError(bindingResult);
        if (errorMsg != null) return errorMsg;

        AccountDTO savedAcc = accountService.saveAccount(accountDTO);
        String otp = authenticationService.generateOTP(savedAcc.getEmail());

        return new ResponseEntity<Response>(Response.builder()
                .status(HttpStatus.OK)
                .message("Register account successfully, please verify the account")
                .build(),
                HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<Response> confirmRegister(@RequestBody String otp, @RequestHeader("Key") String emailKey) {
        boolean isVerfied = authenticationService.verifyOTP(emailKey, otp);
        logger.info(isVerfied + " - " + otp + " - " + emailKey);
        String jwtToken = null;
        try {
            if (isVerfied) {
                AccountDTO registerAcc = accountService.updateAccountStatusActive(emailKey);
                logger.info(registerAcc.toString());
                if (registerAcc == null) throw new Exception("Failed to verify account");
                jwtToken = authenticationService.register(registerAcc);
                logger.info(jwtToken);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Response.builder().status(HttpStatus.BAD_REQUEST).message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Response.builder()
                .status(HttpStatus.OK)
                .message("Verify email account successfully")
                .response("Token", jwtToken)
                .build(),
                HttpStatus.OK);
    }

    public ResponseEntity<Response> login (@Valid @RequestBody Account account, BindingResult bindingResult) {
        ResponseEntity<Response> errorMsg = getResponseError(bindingResult);
        if (errorMsg != null) return errorMsg;

        AccountDTO acc = accountService.findAccountByEmailAndPassword(account.getEmail(), account.getPassword());
        if (acc == null) return new ResponseEntity<>(
                Response.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Invalid email or password")
                        .build(),
                HttpStatus.BAD_REQUEST
        );
        String jwtToken = authenticationService.authenticate(acc);
        return new ResponseEntity<>(Response.builder()
                .status(HttpStatus.OK)
                .message("Login successfully")
                .response("Token", jwtToken)
                .build(),
                HttpStatus.OK);
    }

    private ResponseEntity<Response> getResponseError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMsg= new HashMap<>();
            for(FieldError err: bindingResult.getFieldErrors()){
                errorMsg.put(err.getField(), err.getDefaultMessage());
            }
            Response res = Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Error while processing the request")
                    .responseList(errorMsg)
                    .build();

            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
