package com.example.clase9webservice.repository;

import com.example.clase9webservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
