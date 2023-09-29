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
    public ResponseEntity<String> realizarEmprestimo(@RequestBody EmprestimoRequestDto emprestimoRequestDTO) {
        String nomeLivro = emprestimoRequestDTO.getNomeLivro();
        String nomeUsuario = emprestimoRequestDTO.getNomeUsuario();

        try {
            Emprestimo emprestimo = emprestimoService.realizarEmprestimo(nomeLivro, nomeUsuario);
            return ResponseEntity.ok("Empréstimo realizado com sucesso. ID do empréstimo: " + emprestimo.getId());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Erro ao realizar empréstimo: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/devolucao/{id}")
    public ResponseEntity<String> realizarDevolucao(@PathVariable Long id) {
        try {
            Emprestimo emprestimo = emprestimoService.realizarDevolucao(id);
            return ResponseEntity.ok("Devolução realizada com sucesso para o empréstimo ID: " + emprestimo.getId());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/estender/{id}")
    public ResponseEntity<String> estenderPrazo(@PathVariable Long id){
        try{
            Emprestimo emprestimo = emprestimoService.estenderVencimento(id);
            return ResponseEntity.ok("Vencimento estendido com sucesso para o empréstimo ID: " + emprestimo.getId());
        }
        catch (EntityNotFoundException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<Emprestimo>> findAllLivros(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findLivroById(@PathVariable(value = "id") Long id){
        Optional<Emprestimo> emprestimoOptional = emprestimoService.findById(id);
        if(!emprestimoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmprestimo(@PathVariable(value = "id") Long id){
        Optional<Emprestimo> emprestimoOptional = emprestimoService.findById(id);
        if(!emprestimoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado.");
        }
        emprestimoService.delete(emprestimoOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Empréstimo de id " + id + " deletado corretamente.");
    }

}
