package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.model.MaterialEsportivo;
import edu.ufrn.imd.Biblicron.service.MaterialEsportivoService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

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
        var materialEsportivoLocal = new MaterialEsportivo();

        try{
            BeanUtils.copyProperties(materialEsportivoLocal, materialEsportivo);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(materialEsportivoService.save(materialEsportivoLocal));
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }


    }


}
