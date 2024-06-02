package com.swp391.JewelryProduction.util;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffOrderID implements Serializable {
    private String staffID;
    private String orderID;
}
