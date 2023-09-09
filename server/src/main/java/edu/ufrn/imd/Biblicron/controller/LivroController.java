package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.repository.ILivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {
    @Autowired
    private ILivroRepository livroRepository;

    @GetMapping
    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    @PostMapping
    public void adicionarLivro() {
        // Cria um novo livro com atributos específicos
        Livro novoLivro = new Livro();
        novoLivro.setTitulo("O Nome do Vento");
        novoLivro.setAutor("Patrick Rothfuss");
        // Defina outros atributos, se necessário

        // Insere o novo livro no banco de dados usando o método save()
        livroRepository.save(novoLivro);
    }


    // Implemente outros métodos de controle conforme necessário (atualizar, excluir, buscar por ID, etc.).
}

