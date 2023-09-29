package edu.ufrn.imd.Biblicron.repository;

import edu.ufrn.imd.Biblicron.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByTitulo(String titulo);

    Optional<Livro> findByTitulo(String titulo);
}

