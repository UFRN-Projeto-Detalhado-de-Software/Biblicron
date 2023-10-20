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
}
