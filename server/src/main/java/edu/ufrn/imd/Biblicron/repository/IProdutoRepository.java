package edu.ufrn.imd.Biblicron.repository;

import edu.ufrn.imd.Biblicron.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {
     boolean existsByNomeProduto(String nomeProduto);

     Optional<Produto> findByNomeProduto(String nomeProduto);
}
