package com.example.clase9webservice.controller;

import com.example.clase9webservice.entity.Category;
import com.example.clase9webservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("")
    public List<Category> listaCategorias (){
        return categoryRepository.findAll();
    }
}
