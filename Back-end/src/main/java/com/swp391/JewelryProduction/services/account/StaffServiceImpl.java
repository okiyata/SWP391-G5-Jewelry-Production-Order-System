package com.swp391.JewelryProduction.services.account;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.enums.AccountStatus;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.enums.WorkStatus;
import com.swp391.JewelryProduction.pojos.*;
import com.swp391.JewelryProduction.repositories.StaffRepository;
import com.swp391.JewelryProduction.util.exceptions.ObjectNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService{
    private final StaffRepository staffRepository;

    @Override
    public List<Staff> findAllStaff() {
        return staffRepository.findStaffByRoleEndingWith("STAFF");
    }

    @Override
    public List<Staff> findAllByRole(Role role) {
        return staffRepository.findAllByRole(role);
    }

    @Override
    public Staff findStaffById(String staffId) {
        if(staffRepository.findById(staffId).isPresent()) {
            return staffRepository.findById(staffId).get();
        } else throw new ObjectNotFoundException("Staff with ID: " + staffId +"not found");
    }

    @Override
    public Staff updateStaff(AccountDTO accountDTO, WorkStatus workStatus) {
        Staff staff = setStaff(accountDTO, workStatus);
        if(staffRepository.findById(staff.getId()).isPresent()) {
            staffRepository.save(staff).orElseThrow(() -> new RuntimeException("Staff creation failed"));
            return staff;
        } else throw new ObjectNotFoundException("Staff with ID: " + staff.getId() +"not found");
    }

    @Override
    public Staff createStaff(AccountDTO accountDTO, WorkStatus workStatus) {
        Staff staff = setStaff(accountDTO, workStatus);
        staffRepository.save(staff).orElseThrow(() -> new RuntimeException("Staff creation failed"));
        return staff;
    }
    public Staff setStaff(AccountDTO accountDTO, WorkStatus workStatus) {
        return Staff.builder()
                .id(accountDTO.getId())
                .email(accountDTO.getEmail())
                .password(accountDTO.getPassword())
                .dateCreated(LocalDateTime.now())
                .role(accountDTO.getRole())
                .status(accountDTO.getStatus())
                .userInfo(accountDTO.getUserInfo())
                .pastOrder(accountDTO.getPastOrder())
                .sendingReports(accountDTO.getSendingReports())
                .notifications(accountDTO.getNotifications())
                .currentOrder(accountDTO.getCurrentOrder())
                .workStatus(workStatus)
                .build();
    }

    @Override
    public void deleteStaffById(Staff staff) {
        staffRepository.delete(staff);
    }
}
