package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.repository.IEmprestimoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class EmprestimoService {

    final IEmprestimoRepository emprestimoRepository;

    public EmprestimoService(IEmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }

    @Transactional
    public Emprestimo save(Emprestimo emprestimo) {
        return emprestimoRepository.save(emprestimo);
    }
}
