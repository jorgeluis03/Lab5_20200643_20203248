package com.example.clase9webservice.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductID", nullable = false)
    private Integer id;

    @Column(name = "ProductName", nullable = false, length = 40)
    private String productName;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "SupplierID")
    private Supplier supplierID;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "CategoryID")
    private Category categoryID;

    @Column(name = "QuantityPerUnit", length = 20)
    private String quantityPerUnit;

    @Column(name = "UnitPrice", precision = 10, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "UnitsInStock")
    private Integer unitsInStock;

    @Column(name = "UnitsOnOrder")
    private Integer unitsOnOrder;

    @Column(name = "ReorderLevel")
    private Integer reorderLevel;

    @Column(name = "Discontinued", nullable = false)
    private Boolean discontinued = false;


}