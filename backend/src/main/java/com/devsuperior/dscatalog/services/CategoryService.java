package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Transactional( readOnly = true )
    public CategoryDTO findById( Long id ) {

        Optional<Category> obj = repository.findById( id );

        // if obj category not exists launch exception
        Category entity = obj.orElseThrow( () -> new EntityNotFoundException( "Entity not Found" ) );

        return new CategoryDTO( entity );
    }


}
