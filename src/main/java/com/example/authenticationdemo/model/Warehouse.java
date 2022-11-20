package com.example.authenticationdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

import static com.example.authenticationdemo.model.DbConstants.DB_SCHEMA;


@Entity
@Table(name = "warehouse", schema = DB_SCHEMA)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "warehouse_id", nullable = false)
    private UUID warehouseId;

    @Column(name = "address")
    private String address;

    @Column(name = "is_closed")
    private boolean closed;

    @Column(name = "title")
    private String title;

    @Column(name = "name")
    private String name;

}
