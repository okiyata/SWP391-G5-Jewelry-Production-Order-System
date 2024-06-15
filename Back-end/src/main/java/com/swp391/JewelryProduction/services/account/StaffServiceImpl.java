package com.swp391.JewelryProduction.services.account;

import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.pojos.Staff;
import com.swp391.JewelryProduction.repositories.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService{
    private final StaffRepository staffRepository;

    @Override
    public List<Staff> findAllByRole(Role role) {
        return staffRepository.findAllByRole(role);
    }
}
