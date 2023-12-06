package edu.ufrn.imd.Biblicron.repository;

import edu.ufrn.imd.Biblicron.model.MaterialEsportivo;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IMaterialEsportivoRepository extends JpaRepository<MaterialEsportivo, Long> {

}
