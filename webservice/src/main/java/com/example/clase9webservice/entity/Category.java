package com.example.clase9webservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
//Para ingorar el campo picutre que es muy extenso en la peticion postman
@JsonIgnoreProperties({"picture"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID", nullable = false)
    private Integer id;

    @Column(name = "CategoryName", nullable = false, length = 15)
    private String categoryName;

    @Lob
    @Column(name = "Description")
    private String description;

    @Column(name = "Picture")
    private byte[] picture;

}