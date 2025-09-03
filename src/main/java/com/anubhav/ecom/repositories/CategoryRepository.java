package com.anubhav.ecom.repositories;


import com.anubhav.ecom.models.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}