package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.dto.EmprestimoRequestDto;
import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.service.EmprestimoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/emprestimo")
public class EmprestimoController {
    final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping("/realizar")
    public ResponseEntity<Object> realizarEmprestimo(@RequestBody @Valid EmprestimoRequestDto emprestimoRequestDTO) {
        String nomeLivro = emprestimoRequestDTO.getNomeLivro();
        String nomeUsuario = emprestimoRequestDTO.getNomeUsuario();

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.realizarEmprestimo(nomeLivro, nomeUsuario));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/devolucao/{id}")
    public ResponseEntity<Object> realizarDevolucao(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.realizarDevolucao(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/estender/{id}")
    public ResponseEntity<Object> estenderPrazo(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.estenderVencimento(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/listAll")
    public ResponseEntity<Page<Emprestimo>> findAllEmprestimos(@PageableDefault(page = 0, size = 10, sort = "loanDate", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findEmprestimoById(@PathVariable(value = "id") Long id){
        Optional<Emprestimo> emprestimoOptional = emprestimoService.findById(id);
        if(!emprestimoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: Empréstimo não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoOptional.get());
    }

}
