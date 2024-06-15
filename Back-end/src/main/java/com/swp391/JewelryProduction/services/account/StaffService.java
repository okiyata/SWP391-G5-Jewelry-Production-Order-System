package com.swp391.JewelryProduction.services.account;


import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.pojos.Staff;

import java.util.List;

public interface StaffService {
    List<Staff> findAllByRole(Role role);
}
