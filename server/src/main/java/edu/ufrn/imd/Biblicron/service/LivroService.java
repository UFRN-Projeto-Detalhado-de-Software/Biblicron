package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.model.User;
import edu.ufrn.imd.Biblicron.repository.IEmprestimoRepository;
import edu.ufrn.imd.Biblicron.repository.ILivroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivroService {

    final ILivroRepository livroRepository;
    final IEmprestimoRepository emprestimoRepository;

    public LivroService(ILivroRepository livroRepository, IEmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Transactional
    public Livro save(Livro livro) {

        if(this.existsByTitulo(livro.getTitulo())){
            throw new IllegalStateException("Conflict: Book Title already exists.");
        }
        if(livro.getTitulo().length() > 255){
            throw new IllegalStateException("Length Required: Book Title must have less than 255 characters.");
        }
        if(livro.getAutor().length() > 255){
            throw new IllegalStateException("Length Required: Author's Name must have less than 255 characters.");
        }
        if(livro.getQuantidade() > 1000){
            throw new IllegalStateException("Length Required: Quantity of books must be less than 1000 characters.");
        }
        if(livro.getGeneros() != null && livro.getGeneros().size() > 3){
            throw new IllegalStateException("Length Required: Um livro precisa ter, no máximo, 3 gêneros.");
        }
        if (livro.getGeneros() == null || livro.getGeneros().isEmpty()) {
            throw new IllegalStateException("Length Required: Um livro precisa ter, no mínimo, 1 gênero.");
        }
        if(livro.getPaginas() <= 0){
            throw new IllegalStateException("Length Required: Quantidade de páginas precisa ser maior que 0.");
        }
        if(livro.getDataPublicacao() == null){
            throw new IllegalStateException("Length Required: É necessário informar a data de publicação do livro");
        }

        return livroRepository.save(livro);
    }

    public boolean existsByTitulo(String titulo) {
        return livroRepository.existsByTitulo(titulo);
    }
    public Page<Livro> findAll(Pageable pageable){
        return livroRepository.findAll(pageable);
    }

    public Livro findById(Long id) {
        Optional<Livro> livroOptional = livroRepository.findById(id);
        if(!livroOptional.isPresent()){
            throw new IllegalStateException("Book not found.");
        }
        return livroOptional.get();
    }

    public Optional<Livro> findByTitulo(String titulo){
        return livroRepository.findByTitulo(titulo);
    }
    
    @Transactional
    public Livro update(Long id, Livro livro) {
      
        Optional<Livro> livroOptional = this.findByTitulo(livro.getTitulo());

        Livro savedLivro = this.findById(livroOptional.get().getId());

        if(savedLivro.getId() != id){
            throw new IllegalStateException("Conflict: Book Title is already in use.");
        }

        if(livro.getGeneros() != null && livro.getGeneros().size() > 3){
            throw new IllegalStateException("Length Required: Um livro precisa ter, no máximo, 3 gêneros.");
        }
        if (livro.getGeneros() == null || livro.getGeneros().isEmpty()) {
            throw new IllegalStateException("Length Required: Um livro precisa ter, no mínimo, 1 gênero.");
        }

        savedLivro.setGeneros(livro.getGeneros());

        return this.save(savedLivro);
    }

    @Transactional
    public Livro delete(Long id) {
        Livro livro = this.findById(id);
        livroRepository.delete(livro);;
        return livro;
    }

    public List<Livro> generateSugestoesById(Long id){
        Livro livro = this.findById(id);

        var sugestoes = new HashMap<Livro, Integer>();
        var usuariosPercorridos = new HashMap<User, Boolean>();

        var emprestimos = emprestimoRepository.findEmprestimosByLivro(id);
        for (Emprestimo emprestimo : emprestimos) {
            var usuario = emprestimo.getUsuario();
            if (usuariosPercorridos.containsKey(usuario)) {
                continue;
            }
            usuariosPercorridos.put(usuario, true);
            var emprestimosUsuario = emprestimoRepository.findEmprestimosByUsuario(usuario.getId());
            for (var emprestimoUsuario : emprestimosUsuario) {
                if (emprestimoUsuario.getLivro().getId() == livro.getId()) {
                    continue;
                }
                sugestoes.put(emprestimoUsuario.getLivro(), sugestoes.getOrDefault(emprestimoUsuario.getLivro(), 0) + 1);
            }
        }
        System.out.println("Sugestões para o livro " + livro.getTitulo() + ":");
        var sortedSugestions = sugestoes.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).collect(Collectors.toList());
        for (var sugestao : sortedSugestions) {
            System.out.println("Pessoas que pegaram este livro também pegaram " + sugestao.getKey().getTitulo() + " num total de " + sugestao.getValue() + " vezes.");
        }
        return sortedSugestions.stream().map(sugestao -> sugestao.getKey()).collect(Collectors.toList());
    }
}
