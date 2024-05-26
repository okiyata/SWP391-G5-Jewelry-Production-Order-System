package com.swp391.JewelryProduction.dto;

import com.swp391.JewelryProduction.enums.ReportType;
import com.swp391.JewelryProduction.pojos.Account;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReportDTO {
    private Integer id;
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private ReportType type;
    private Account sender;
}
