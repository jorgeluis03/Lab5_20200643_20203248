package com.example.clase9webservice.controller;

import com.example.clase9webservice.entity.Supplier;
import com.example.clase9webservice.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    SupplierRepository supplierRepository;

    @GetMapping("")
    public List<Supplier> listaProductosSupplier(){
        return supplierRepository.findAll();
    }

}
