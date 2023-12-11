package edu.ufrn.imd.Biblicron.repository;

import edu.ufrn.imd.Biblicron.model.DispositivoEletronico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IDispositivoEletronicoRepository extends JpaRepository<DispositivoEletronico, Long> {

    Optional<DispositivoEletronico> findByNomeProduto(String nomeProduto);

    Optional<DispositivoEletronico> findBySerialNumber(String serialNumber);

}
