package com.devsuperior.dscatalog.services;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.resources.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
//import com.devsuperior.dscatalog.dto.UriDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
//import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
    private static final long serialVersion = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired      // injeção geranciada pelo spring
    private ProductRepository repository;

    @Transactional( readOnly = true )   // readOnly não faz lock no banco
    public Page<ProductDTO> findAllPaged( PageRequest pageRequest ){
        Page<Product> list = repository.findAll( pageRequest );
        //return list.map( x -> new ProductDTO( x ) );
        return list.map( ProductDTO::new );    // method reference = lambda
    }

    @Transactional( readOnly = true )
    public ProductDTO findById( Long id ) {

        Optional<Product> obj = repository.findById( id );

        // if obj category not exists launch exception
        Product entity = obj.orElseThrow( () -> new ResourceNotFoundException( "Entity not Found" ) );

        return new ProductDTO( entity, entity.getCategories() );
    }

    @Transactional
    public ProductDTO insert( ProductDTO dto ) {
        Product entity = new Product();
        copyDtoToEntity( dto, entity );
        entity = repository.save( entity );
        return new ProductDTO( entity );
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for ( CategoryDTO catDto : dto.getCategories() ) {
            Category category = categoryRepository.getOne( catDto.getId() );
            entity.getCategories().add( category );
        }
    }

    @Transactional
    public ProductDTO update( Long id, ProductDTO dto ) {
        try {
            Product entity = repository.getOne( id );
            entity.setName(dto.getName());
            entity = repository.save( entity );

            return new ProductDTO( entity );
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