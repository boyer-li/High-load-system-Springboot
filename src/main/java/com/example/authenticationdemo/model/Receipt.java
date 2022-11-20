package com.example.authenticationdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

import static com.example.authenticationdemo.model.DbConstants.DB_SCHEMA;


@Entity
@Table(name = "receipt", schema = DB_SCHEMA)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "receipt_id", nullable = false)
    private UUID receiptId;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User idUser;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "created_datetime", nullable = false)
    private Instant createdDatetime;

}
