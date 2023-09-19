package edu.ufrn.imd.Biblicron.repository;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmprestimoRepository extends JpaRepository<Emprestimo, Long> {
}
