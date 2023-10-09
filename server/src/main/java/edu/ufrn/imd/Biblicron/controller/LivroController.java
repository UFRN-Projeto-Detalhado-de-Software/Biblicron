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
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<Page<Livro>> findAllLivros(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(livroService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findLivroById(@PathVariable(value = "id") Long id){
        Optional<Livro> livroOptional = livroService.findById(id);
        if(!livroOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(livroOptional.get());
    }

    @GetMapping("/{id}/sugestoes")
    public ResponseEntity<Object> generateSugestoesById(@PathVariable(value = "id") Long id){

        try {
            List<Livro> sugestoes = this.livroService.generateSugestoesById(id);
            return ResponseEntity.status(HttpStatus.OK).body(sugestoes);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
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

        //Checando se um livro já existe com esse título e se o id do livro é diferente do livro que o usuário deseja editar
        Optional<Livro> livroByTitulo = livroService.findByTitulo(livroDto.getTitulo());
        if(livroByTitulo.isPresent() && livroByTitulo.get().getId() != id){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Book Title is already in use.");
        }

        if(livroDto.getGeneros() != null && livroDto.getGeneros().size() > 3){
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("Length Required: Um livro precisa ter, no máximo, 3 gêneros.");
        }
        if (livroDto.getGeneros() == null || livroDto.getGeneros().isEmpty()) {
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("Length Required: Um livro precisa ter, no mínimo, 1 gênero.");
        }

        Optional<Livro> livroOptional = livroService.findById(id);
        if(!livroOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        var livro = livroOptional.get();
        try {
            BeanUtils.copyProperties(livro, livroDto);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        livro.setId(livroOptional.get().getId());

        // Configure os gêneros do livro
        livro.setGeneros(livroDto.getGeneros());

        return ResponseEntity.status(HttpStatus.OK).body(livroService.save(livro));
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
