package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.model.User;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.dto.EmprestimoDto;
import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.service.LivroService;
import edu.ufrn.imd.Biblicron.service.UserService;
import edu.ufrn.imd.Biblicron.service.EmprestimoService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/emprestimo")
public class EmprestimoController {
    final LivroService livroService;
    final UserService userService;
    final EmprestimoService emprestimoService;

    public EmprestimoController(
      LivroService livroService,
      UserService userService,
      EmprestimoService emprestimoService
    ) {
        this.livroService = livroService;
        this.userService = userService;
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<Object> emprestimo(@RequestBody @Valid EmprestimoDto emprestimoDto){
      Optional<User> userOptional = userService.findById(emprestimoDto.getUserId());
      System.out.println(emprestimoDto.getUserPass());
      Optional<User> userBiblioOptional = userService.findById(emprestimoDto.getUserBiblioId());
      Optional<Livro> livroOptional = livroService.findById(emprestimoDto.getIdLivro());

      if(!userOptional.isPresent()){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
      }
      if(!userBiblioOptional.isPresent()){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User-biblio not found.");
      }
      if(!emprestimoDto.getUserPass().equals(userBiblioOptional.get().getPassword())
        && userBiblioOptional.get().getUserType() == "BIBLIO"
      ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User-biblio credentials invalid.");
      }
      if(!livroOptional.isPresent()){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
      }
      if(!livroOptional.get().ehDisponivel()){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not enabel.");
      }

      var emprestimo = new Emprestimo();
      try {
        BeanUtils.copyProperties(emprestimo, emprestimoDto);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
      return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.save(emprestimo));
    }
}

