package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// components usam @Service, @Component, @Service
// gerencia a injeção de depedências
@Service
public class CategoryService {

    @Autowired      // injeção geranciada pelo spring
    private CategoryRepository repository;

    public List<Category> findAll(){
        return repository.findAll();
    }
}
