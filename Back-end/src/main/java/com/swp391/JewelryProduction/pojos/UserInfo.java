package com.swp391.JewelryProduction.pojos;

import com.swp391.JewelryProduction.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "User_Info")
public class UserInfo {
    @Id
    private String id;

    @Column(nullable = false, name = "first_name", columnDefinition = "nvarchar(200)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "nvarchar(200)")
    private String lastName;

    @Column(columnDefinition = "date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private long phoneNumber;

    private String address;

    @OneToOne(mappedBy = "userInfo", fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id", columnDefinition = "nvarchar(8)")
    private Account account;
}
