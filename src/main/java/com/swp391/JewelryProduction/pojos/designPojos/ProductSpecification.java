package com.swp391.JewelryProduction.pojos.designPojos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_specification")
public class ProductSpecification {
    @Id
    @GeneratedValue
    private Integer id;

    private String type;

    private String style;

    private String occasion;

    private String length;

    private String metal;

    private String texture;

    private String chainType;

    private String gemstone;

    private String shape;

    private String gemstoneWeight;
}
