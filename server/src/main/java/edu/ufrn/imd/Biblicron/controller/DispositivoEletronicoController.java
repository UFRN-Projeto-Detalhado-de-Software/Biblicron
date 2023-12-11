package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.model.DispositivoEletronico;
import edu.ufrn.imd.Biblicron.model.MaterialEsportivo;
import edu.ufrn.imd.Biblicron.service.DispositivoEletronicoService;
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
@RequestMapping("/dispositivo-eletronico")
public class DispositivoEletronicoController{
    final DispositivoEletronicoService dispositivoEletronicoService;


    public DispositivoEletronicoController(DispositivoEletronicoService dispositivoEletronicoService) {
        this.dispositivoEletronicoService = dispositivoEletronicoService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DispositivoEletronico dispositivoEletronico){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(dispositivoEletronicoService.save(dispositivoEletronico));
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/listAll")
    public ResponseEntity<Page<DispositivoEletronico>> findAllDispositivoEletronico(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(dispositivoEletronicoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findDispositivoEletronicoById(@PathVariable(value = "id") Long id){
        Optional<DispositivoEletronico> dispositivoEletronicoOptional = dispositivoEletronicoService.findById(id);
        if(!dispositivoEletronicoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(dispositivoEletronicoOptional.get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteDispositivoEletronico(@PathVariable(value = "id") Long id){
        Optional<DispositivoEletronico> dispositivoEletronicoOptional = dispositivoEletronicoService.findById(id);
        if(!dispositivoEletronicoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        try{
            dispositivoEletronicoService.delete(dispositivoEletronicoOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Produto de id:  " + id + " deletado com sucesso.");
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateDispositivoEletronico(@PathVariable(value = "id") Long id,
                                                          @RequestBody @Valid DispositivoEletronico dispositivoEletronico){

        dispositivoEletronico.setId(id);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(dispositivoEletronicoService.update(dispositivoEletronico));
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
