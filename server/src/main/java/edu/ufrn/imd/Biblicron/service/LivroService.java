package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.model.User;
import edu.ufrn.imd.Biblicron.repository.IEmprestimoRepository;
import edu.ufrn.imd.Biblicron.repository.ILivroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
        ArrayList<String> errosLog = new ArrayList<>();

        if(existsByTitulo(livro.getTitulo())){
            errosLog.add("Conflict: Book Title is already in use.");
        }
        if(livro.getTitulo().length() > 255){
            errosLog.add("Length Required: Book Title must have less than 255 characters.");
        }
        if(livro.getAutor().length() > 255){
            errosLog.add("Length Required: Author's Name must have less than 255 characters.");
        }
        if(livro.getQuantidade() > 1000){
            errosLog.add("Length Required: Quantity of books must be less than 1000 characters.");
        }
        if(livro.getGeneros() != null && livro.getGeneros().size() > 3){
            errosLog.add("Length Required: Um livro precisa ter, no máximo, 3 gêneros.");
        }
        if (livro.getGeneros() == null || livro.getGeneros().isEmpty()) {
            errosLog.add("Length Required: Um livro precisa ter, no mínimo, 1 gênero.");
        }
        if(livro.getPaginas() <= 0){
            errosLog.add("Length Required: Quantidade de páginas precisa ser maior que 0.");
        }
        if(livro.getDataPublicacao() == null){
            errosLog.add("Length Required: É necessário informar a data de publicação do livro");
        }
        if(livro.getQuantidade() <= 0){
            errosLog.add("Length Required: Quantidade de livros precisa ser maior que 0.");
        }
        if(livro.getQuantidadeDisponivel() > livro.getQuantidade()){
            errosLog.add("Length Required: Quantidade Disponível de livros precisa ser menor que a quantidade total de livros.");
        }

        if(!errosLog.isEmpty()){
            throw new IllegalStateException(String.valueOf(errosLog));
        }

        livro.setQuantidadeDisponivel(livro.getQuantidade());
        return livroRepository.save(livro);
    }

    public boolean existsByTitulo(String titulo) {
        return livroRepository.existsByTitulo(titulo);
    }
    public Page<Livro> findAll(Pageable pageable){
        return livroRepository.findAll(pageable);
    }

    public Optional<Livro> findById(Long id) {
        return livroRepository.findById(id);
    }

    public Optional<Livro> findByTitulo(String titulo){
        return livroRepository.findByTitulo(titulo);
    }

    @Transactional
    public void delete(Livro livro) {
        List<Emprestimo> currentUsage = emprestimoRepository.findByLivroAndReturnDateIsNull(livro);
        List<Emprestimo> isAtLoanLog = emprestimoRepository.findByLivro(livro);
        if(!(currentUsage.isEmpty())){
            throw new IllegalStateException("Conflict: Livro " + livro.getTitulo() + " está emprestado");
        }
        if(!(isAtLoanLog.isEmpty())){
            throw new IllegalStateException("Conflict: Livro " + livro.getTitulo() + " está nos registros de empréstimo e não pode ser apagado");
        }
        livroRepository.delete(livro);
    }

    public List<Livro> generateSugestoesById(Long id){
        var livro = livroRepository.findById(id).get();

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

    @Transactional
    public Livro update(Livro livro) {
        ArrayList<String> errosLog = new ArrayList<>();

        Optional<Livro> livroByTitulo = findByTitulo(livro.getTitulo());

        if(livroByTitulo.isPresent() && livroByTitulo.get().getId() != livro.getId()){
            errosLog.add("Conflict: Book Title is already in use.");
        }

        Optional<Livro> livroOptional = findById(livro.getId());
        if(!livroOptional.isPresent()){
            errosLog.add("Not Found: Book not found.");
        }

        if(livro.getTitulo().length() > 255){
            errosLog.add("Length Required: Book Title must have less than 255 characters.");
        }
        if(livro.getAutor().length() > 255){
            errosLog.add("Length Required: Author's Name must have less than 255 characters.");
        }
        if(livro.getQuantidade() > 1000){
            errosLog.add("Length Required: Quantity of books must be less than 1000 characters.");
        }
        if(livro.getGeneros() != null && livro.getGeneros().size() > 3){
            errosLog.add("Length Required: Um livro precisa ter, no máximo, 3 gêneros.");
        }
        if (livro.getGeneros() == null || livro.getGeneros().isEmpty()) {
            errosLog.add("Length Required: Um livro precisa ter, no mínimo, 1 gênero.");
        }
        if(livro.getPaginas() <= 0){
            errosLog.add("Length Required: Quantidade de páginas precisa ser maior que 0.");
        }
        if(livro.getDataPublicacao() == null){
            errosLog.add("Length Required: É necessário informar a data de publicação do livro");
        }
        if(livro.getQuantidade() <= 0){
            errosLog.add("Length Required: Quantidade de livros precisa ser maior que 0.");
        }
        if(livro.getQuantidadeDisponivel() <= 0){
            errosLog.add("Length Required: Quantidade Disponível de livros precisa ser maior que 0.");
        }
        if(livro.getQuantidadeDisponivel() > livro.getQuantidade()){
            errosLog.add("Length Required: Quantidade Disponível de livros precisa ser menor que a quantidade total de livros.");
        }

        if(!errosLog.isEmpty()){
            throw new IllegalStateException(String.valueOf(errosLog));
        }

        return livroRepository.save(livro);
    }
}
