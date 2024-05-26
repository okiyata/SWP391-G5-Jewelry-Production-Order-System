package com.swp391.JewelryProduction.dto;

import com.swp391.JewelryProduction.pojos.Account;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatusDTO {
    private Integer id;
    private String statusName;
    private StatusType type;
    private List<Account> accountList;
}
