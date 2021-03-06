package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value="/products")
public class ProductResource {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
        ) {

        PageRequest pageRequest = PageRequest.of( page, linesPerPage, Sort.Direction.valueOf( direction ), orderBy );
        Page<ProductDTO> list = service.findAllPaged( pageRequest );
//        list.add(new Product(1L, "Books"));
//        list.add(new Product(1L, "Electronics"));

        return ResponseEntity.ok().body(list);
    }

    @GetMapping( value = "/{id}")   // tem que ser igual o param e ter @PathVariable
    public ResponseEntity<ProductDTO> findById( @PathVariable Long id ) {
        ProductDTO dto  = service.findById( id );

        return ResponseEntity.ok().body( dto );
    }

    // 200 requisição sucesso
    // 201 inserção sucesso, convem informar no header o endereço
    // 204 success e corpo da resposta vazio
    @PostMapping
    public ResponseEntity<ProductDTO> insert( @RequestBody ProductDTO dto ) { // @RequestBody o endpoint reconheça objeto e case com dto
        dto  = service.insert( dto );
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path( "/{id}" )
                .buildAndExpand( dto.getId() ).toUri();
        return ResponseEntity.created( uri ).body( dto );
    }

    @PutMapping( value = "/{id}" )
    public ResponseEntity<ProductDTO> update( @PathVariable Long id, @RequestBody ProductDTO dto ) {
        dto  = service.update( id, dto );
        return ResponseEntity.ok().body( dto );
    }

    @DeleteMapping( value = "/{id}" )
    public ResponseEntity<ProductDTO> delete( @PathVariable Long id ) {
        service.delete( id );
        return ResponseEntity.noContent().build();
    }
}
