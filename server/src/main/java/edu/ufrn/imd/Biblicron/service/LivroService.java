package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.repository.ILivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    final ILivroRepository livroRepository;

    public LivroService(ILivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Transactional
    public Livro save(Livro livro) {
        return livroRepository.save(livro);
    }

    public boolean existsByTitulo(String titulo) {
        return livroRepository.existsByTitulo(titulo);
    }
    public List<Livro> findAll(){
        return livroRepository.findAll();
    }

    public Optional<Livro> findById(Long id) {
        return livroRepository.findById(id);
    }

    @Transactional
    public void delete(Livro livro) {
        livroRepository.delete(livro);
    }
}
