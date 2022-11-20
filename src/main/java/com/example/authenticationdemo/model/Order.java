package com.example.authenticationdemo.model;

import com.example.authenticationdemo.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.UUID;

import static com.example.authenticationdemo.model.DbConstants.DB_SCHEMA;


@Entity
@Table(name = "orders", schema = DB_SCHEMA)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus status;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id_link")
//    private OrderProductId idLink;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id",nullable = false)
    private Product idProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User idUser;

    @Column(name = "created_datetime", nullable = false)
    private String createdDatetime;

    @Column(name = "product_count")
    private int productCount;



}
