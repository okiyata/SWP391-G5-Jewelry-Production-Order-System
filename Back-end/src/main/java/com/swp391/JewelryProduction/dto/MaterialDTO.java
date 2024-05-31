package com.swp391.JewelryProduction.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "material_price_list")
public class MaterialDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private LocalDateTime crawlTime; // Add the crawlTime field

    public MaterialDTO(String name, String price, LocalDateTime crawlTime) {
        this.name = name;
        this.price = price;
        this.crawlTime = crawlTime;
    }

    @Override
    public String toString() {
        return "Component {" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", crawlTime='" + crawlTime + '\'' +
                '}';
    }
}
