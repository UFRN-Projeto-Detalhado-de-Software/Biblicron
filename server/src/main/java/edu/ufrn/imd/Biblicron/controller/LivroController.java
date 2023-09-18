package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.dto.LivroDto;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.service.LivroService;
import org.apache.catalina.mbeans.MBeanUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/livro")
public class LivroController {
    final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<Object> saveLivro(@RequestBody @Valid LivroDto livroDto){
        if(livroService.existsByTitulo(livroDto.getTitulo())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Book Title is already in use.");
        }
        if(livroDto.getTitulo().length() > 255){
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("Length Required: Book Title must have less than 255 characters.");
        }
        if(livroDto.getAutor().length() > 255){
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("Length Required: Author's Name must have less than 255 characters.");
        }
        if(livroDto.getQuantidade() > 1000){
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("Length Required: Quantity of books must be less than 1000 copies.");
        }

        var livro = new Livro();
        try {
            BeanUtils.copyProperties(livro, livroDto);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.save(livro));
    }

    @GetMapping
    public ResponseEntity<Page<Livro>> getAllLivros(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(livroService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLivroById(@PathVariable(value = "id") Long id){
        Optional<Livro> livroOptional = livroService.findById(id);
        if(!livroOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(livroOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLivro(@PathVariable(value = "id") Long id){
        Optional<Livro> livroOptional = livroService.findById(id);
        if(!livroOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        livroService.delete(livroOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Book of id " + id + " deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLivro(@PathVariable(value = "id") Long id,
                                              @RequestBody @Valid LivroDto livroDto){

        Optional<Livro> livroOptional = livroService.findById(id);
        if(!livroOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }

        //Livro receives livroOptional properties, if there is a book with the id in the database.
        var livro = livroOptional.get();

        if(livroService.existsByTitulo(livroDto.getTitulo()) && !Objects.equals(livro.getTitulo(), livroDto.getTitulo())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Book Title is already in use.");
        }

        try {
            BeanUtils.copyProperties(livro, livroDto);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        livro.setId(livroOptional.get().getId());

        return ResponseEntity.status(HttpStatus.OK).body(livroService.save(livro));
    }
}

