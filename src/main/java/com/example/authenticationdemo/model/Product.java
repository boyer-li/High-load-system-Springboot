package com.example.authenticationdemo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

import static com.example.authenticationdemo.model.DbConstants.DB_SCHEMA;


@Entity
@Table(name = "product", schema = DB_SCHEMA)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id_link")
//    private OrderProductId idLink;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "count")
    private int count;


}
