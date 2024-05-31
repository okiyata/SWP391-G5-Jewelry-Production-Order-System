package com.swp391.JewelryProduction.pojos;

import com.swp391.JewelryProduction.enums.WorkStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("1")
public class Staff extends Account{
    private WorkStatus wStatus;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.REMOVE)
    private List<StaffOrderHistory> history;
}
