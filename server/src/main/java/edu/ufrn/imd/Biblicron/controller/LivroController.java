package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.dto.EstipularPrazoRequest;
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
        var livro = new Livro();
        livro.setGeneros(livroDto.getGeneros());
        livro.setQuantidadeDisponivel(livroDto.getQuantidade());

        try {
            BeanUtils.copyProperties(livro, livroDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(livroService.save(livro));
        } catch (IllegalAccessException | InvocationTargetException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<Livro>> findAllLivros(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(livroService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findLivroById(@PathVariable(value = "id") Long id){

        try {
            Livro livro = livroService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(livro);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/sugestoes")
    public ResponseEntity<Object> generateSugestoesById(@PathVariable(value = "id") Long id){

        try {
            List<Livro> sugestoes = this.livroService.generateSugestoesById(id);
            return ResponseEntity.status(HttpStatus.OK).body(sugestoes);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLivro(@PathVariable(value = "id") Long id){
        
        try {
            Livro deleted = this.livroService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Book of id " + deleted.getId() + " deleted successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLivro(@PathVariable(value = "id") Long id,
                                              @RequestBody @Valid LivroDto livroDto){

        var livro = new Livro();
        try {
            BeanUtils.copyProperties(livro, livroDto);
            Livro livroAtualizado = this.livroService.update(id, livro);
            return ResponseEntity.status(HttpStatus.OK).body(livroAtualizado);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/estipularPrazoDia")
    public ResponseEntity<Object> estipularPrazoDias(@RequestBody EstipularPrazoRequest estipularPrazoRequest){
        if(estipularPrazoRequest.getParametro() <= 0){
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("Length Required: a quantidade de dias precisa ser maior que 0");
        }
        if(estipularPrazoRequest.getNomeLivro() == null){
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("Length Required: é necessário informar o nome do livro");
        }

        Optional<Livro> livroOptional = livroService.findByTitulo(estipularPrazoRequest.getNomeLivro());
        if (!livroOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }

        Livro livro = livroOptional.get();
        int paginas = livro.getPaginas();

        // Calcula quantas páginas o usuário precisa ler por dia para terminar em quantidadeDias
        int paginasPorDia = calcularTempoDias(paginas, estipularPrazoRequest.getParametro());

        return ResponseEntity.status(HttpStatus.OK).body("Você precisa ler " + paginasPorDia + " páginas por dia para terminar em " + estipularPrazoRequest.getParametro() + " dias. O livro tem um total de " + livro.getPaginas() + " páginas.");
    }

    @PostMapping("/estipularPrazoPaginas")
    public ResponseEntity<Object> estipularPrazoPaginas(@RequestBody EstipularPrazoRequest estipularPrazoRequest){
        if(estipularPrazoRequest.getParametro() <= 0){
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("Length Required: a quantidade de páginas precisa ser maior que 0");
        }
        if(estipularPrazoRequest.getNomeLivro() == null){
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("Length Required: é necessário informar o nome do livro");
        }

        Optional<Livro> livroOptional = livroService.findByTitulo(estipularPrazoRequest.getNomeLivro());
        if (!livroOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }

        Livro livro = livroOptional.get();
        int paginas = livro.getPaginas();

        // Calcula quantos dias o usuário levará para ler o livro com a taxa de leitura informada
        int quantidadeDias = calcularTempoPaginas(paginas, estipularPrazoRequest.getParametro());

        return ResponseEntity.status(HttpStatus.OK).body("Você terminará a leitura em " + quantidadeDias + " dias se ler " + estipularPrazoRequest.getParametro() + " páginas por dia. O livro tem um total de " + livro.getPaginas() + " páginas.");
    }


    private int calcularTempoDias(int paginas, int quantidadeDias) {
        // Calcula quantas páginas o usuário precisa ler por dia para terminar em quantidadeDias
        return Math.round((float) paginas / quantidadeDias);
    }

    private int calcularTempoPaginas(int paginas, int paginasPorDia) {
        // Calcula quantos dias o usuário levará para ler o livro com a taxa de leitura informada
        return Math.round((float) paginas / paginasPorDia);
    }

}
