package edu.ufrn.imd.Biblicron.repository;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    Optional<Emprestimo> findByUsuarioAndReturnDateIsNull(User usuario);
    @Query("SELECT e FROM Emprestimo e WHERE e.maxReturnDate >= :dataLimite AND e.maxReturnDate <= :dataLimitePlusOneDay")
    List<Emprestimo> findByMaxReturnDateWithin24Hours(@Param("dataLimite") LocalDate dataLimite, @Param("dataLimitePlusOneDay") LocalDate dataLimitePlusOneDay);


}
