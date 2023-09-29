package edu.ufrn.imd.Biblicron.repository;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    Optional<Emprestimo> findByUsuarioAndReturnDateIsNull(User usuario);

}
