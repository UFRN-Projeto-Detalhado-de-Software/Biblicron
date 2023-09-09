package edu.ufrn.imd.Biblicron.repository;
import edu.ufrn.imd.Biblicron.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILivroRepository extends JpaRepository<Livro, Long> {
    // Você pode adicionar métodos de consulta personalizados aqui, se necessário.
}

