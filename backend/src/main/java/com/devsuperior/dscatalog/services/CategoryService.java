package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// components usam @Service, @Component, @Service
// gerencia a injeção de depedências
@Service
public class CategoryService implements Serializable {
    private static final long serialVersion = 1L;

    @Autowired      // injeção geranciada pelo spring
    private CategoryRepository repository;

    @Transactional( readOnly = true )   // readOnly não faz lock no banco
    public List<CategoryDTO> findAll(){

        List<Category> list = repository.findAll();
        List<CategoryDTO> listDTO = list.stream()
                .map( x -> new CategoryDTO( x ) ).collect( Collectors.toList() );



//        for( Category cat : list ){
//            listDTO.add( new CategoryDTO( cat ) );
//        }
        return listDTO;
    }
}
