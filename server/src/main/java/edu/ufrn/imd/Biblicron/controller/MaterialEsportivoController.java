package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.dto.LivroDto;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.model.MaterialEsportivo;
import edu.ufrn.imd.Biblicron.service.MaterialEsportivoService;
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
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/material-esportivo")
public class MaterialEsportivoController {

    final MaterialEsportivoService materialEsportivoService;


    public MaterialEsportivoController(MaterialEsportivoService materialEsportivoService) {
        this.materialEsportivoService = materialEsportivoService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid MaterialEsportivo materialEsportivo){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(materialEsportivoService.save(materialEsportivo));
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/listAll")
    public ResponseEntity<Page<MaterialEsportivo>> findAllMateriaisEsportivos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(materialEsportivoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findMaterialEsportivoById(@PathVariable(value = "id") Long id){
        Optional<MaterialEsportivo> materialEsportivoOptional = materialEsportivoService.findById(id);
        if(!materialEsportivoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(materialEsportivoOptional.get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteMaterialEsportivo(@PathVariable(value = "id") Long id){
        Optional<MaterialEsportivo> materialEsportivoOptional = materialEsportivoService.findById(id);
        if(!materialEsportivoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        try{
            materialEsportivoService.delete(materialEsportivoOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Produto de id:  " + id + " deletado com sucesso.");
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateMaterialEsportivo(@PathVariable(value = "id") Long id,
                                              @RequestBody @Valid MaterialEsportivo materialEsportivo){

        var materialEsportivoLocal = new MaterialEsportivo();
        try {
            BeanUtils.copyProperties(materialEsportivoLocal, materialEsportivo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        materialEsportivoLocal.setId(id);

        // Configure os tamanhos de material esportivo
        materialEsportivoLocal.setTamanhos(materialEsportivo.getTamanhos());

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(materialEsportivoService.update(materialEsportivoLocal));
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


}
