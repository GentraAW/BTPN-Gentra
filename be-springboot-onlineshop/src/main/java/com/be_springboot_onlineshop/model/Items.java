package com.be_springboot_onlineshop.model;
import lombok.*;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items", schema = "public")
public class Items {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "price")
    private Integer price;
    
    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "last_re_stock")
    private Date lastReStock;
}
