package com.swp391.JewleryProduction.dto;

import com.swp391.JewleryProduction.enums.Gender;
import com.swp391.JewleryProduction.enums.Role;
import com.swp391.JewleryProduction.pojos.Status;
import com.swp391.JewleryProduction.util.IdGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
<<<<<<< Updated upstream
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.lang.NonNull;
=======
>>>>>>> Stashed changes

import javax.annotation.MatchesPattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
<<<<<<< Updated upstream
public class AccountDTO {
=======
public class AccountDTO{
>>>>>>> Stashed changes
    private String id;
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Email is invalid")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password is invalid")
    @NotEmpty(message = "Password cannot by empty")
    private String password;
    private LocalDateTime dateCreated;
    private Role role;
<<<<<<< Updated upstream
    private Status status;
=======
    private AccountStatus status;
>>>>>>> Stashed changes
}
