package com.swp391.JewelryProduction.pojos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Notification_Board")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", columnDefinition = "nvarchar(8)")
    private Account account;
}
