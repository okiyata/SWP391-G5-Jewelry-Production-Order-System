package com.swp391.JewelryProduction.security.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.enums.AccountStatus;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.security.auth.AuthenticationRequest;
import com.swp391.JewelryProduction.security.auth.AuthenticationResponse;
import com.swp391.JewelryProduction.security.auth.RegisterRequest;
import com.swp391.JewelryProduction.security.model.User;
import com.swp391.JewelryProduction.security.model.UserRepository;
import com.swp391.JewelryProduction.services.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    private final Integer EXPIRE_MINS = 5;
    private LoadingCache<String, String> otpCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return "";
                }
            });

    public String register(AccountDTO accountDTO) {
        var user = User.builder()
                .account(modelMapper.map(accountDTO, Account.class))
                .build();
        userRepository.save(user);
        return jwtService.generateToken(user);
    }

    public String authenticate(AccountDTO accountDTO) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    accountDTO.getEmail(),
                    accountDTO.getPassword()
            )
        );
        String jwtToken = null;
        try {
            var user = userRepository.findByEmail(accountDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("No user found"));
            jwtToken = jwtService.generateToken(user);
        } catch (Exception ex) {logger.warn(ex.getMessage());}

        return jwtToken;
    }

    public String generateOTP (String emailKey) {
        Random rand = new Random();
        StringBuilder otp = new StringBuilder(String.valueOf(rand.nextInt(999999)));
        while (otp.length() < 6) {
            otp.insert(0, "0");
        }
        logger.info(otp.toString() + " " + emailKey);
        otpCache.put(emailKey, otp.toString());
        logger.info(otpCache.getIfPresent(emailKey));
        return otp.toString();
    }

    public boolean verifyOTP (String emailKey, String otp) {
        logger.info(otpCache.getIfPresent(emailKey));
        String savedOTP = otpCache.getIfPresent(emailKey);
        logger.info(savedOTP);
        otpCache.invalidate(emailKey);
        logger.info(savedOTP + " " + otp);
        return savedOTP != null && savedOTP.equals(otp);
    }
}
