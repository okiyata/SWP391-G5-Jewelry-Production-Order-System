package com.swp391.JewelryProduction.pojos;

import com.swp391.JewelryProduction.util.IdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue(generator = "product_seq")
    @GenericGenerator(
            name = "product_seq",
            type = IdGenerator.class,
            parameters = {
                    @Parameter(name = IdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "PRO")
            }
    )
    private String id;
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_specification",
            joinColumns = { @JoinColumn(name = "product_id") },
            inverseJoinColumns = { @JoinColumn(name = "value_id")}
    )
    private List<ParameterValue> productValues;
}
