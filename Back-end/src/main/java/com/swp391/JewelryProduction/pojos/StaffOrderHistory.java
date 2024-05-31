package com.swp391.JewelryProduction.pojos;

import com.swp391.JewelryProduction.util.StaffOrderID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Staff_Order_History")
@IdClass(StaffOrderID.class)
public class StaffOrderHistory {
    @Id
    @Column(name = "staff_id", columnDefinition = "nvarchar(8)")
    private String staffID;
    @Id
    @Column(name = "order_id", columnDefinition = "nvarchar(8)")
    private String orderID;
    @Column(name = "start_date", nullable = false, columnDefinition = "datetime")
    private LocalDateTime startDate;
    @Column(name = "end_date", columnDefinition = "datetime")
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

}
