package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.resources.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
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
        Category entity = obj.orElseThrow( () -> new ResourceNotFoundException( "Entity not Found" ) );

        return new CategoryDTO( entity );
    }

    @Transactional
    public CategoryDTO insert( CategoryDTO dto ) {
        Category entity = new Category();
        entity.setName( dto.getName() );
        entity = repository.save( entity );
        return new CategoryDTO( entity );
    }

    @Transactional
    public CategoryDTO update( Long id, CategoryDTO dto ) {
        try {
            Category entity = repository.getOne( id );
            entity.setName(dto.getName());
            entity = repository.save( entity );

            return new CategoryDTO( entity );
        }catch( EntityNotFoundException e ){
            throw new ResourceNotFoundException( "Id not found " + id );
        }
    }

    public void delete( Long id ) {
        try{
            repository.deleteById( id );
        }catch( EmptyResultDataAccessException e ){
            throw new ResourceNotFoundException( "Id not found " + id );
        }catch( DataIntegrityViolationException e ){
            throw new DatabaseException( "Integrity violation ");   // caso uma categoria possa e deixar um produto sem categoria
        }
    }
}
