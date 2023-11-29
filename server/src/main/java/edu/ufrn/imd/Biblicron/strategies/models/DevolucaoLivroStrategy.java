package edu.ufrn.imd.Biblicron.strategies.models;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.strategies.interfaces.IDevolucaoStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DevolucaoLivroStrategy implements IDevolucaoStrategy {
    @Override
    public boolean canBeReturned(Emprestimo emprestimo) {
        return emprestimo.getReturnDate() == null && !LocalDate.now().isAfter(emprestimo.getMaxReturnDate());
    }

    @Override
    public boolean isReturned(Emprestimo emprestimo){
        return emprestimo.getReturnDate() != null;
    }

    @Override
    public boolean isLate(Emprestimo emprestimo){
        return emprestimo.getMaxReturnDate().isBefore(LocalDate.now());
    }

    @Override
    public boolean canBeExtended(Emprestimo emprestimo) {
        return LocalDate.now().isBefore(emprestimo.getMaxReturnDate()) || LocalDate.now().isEqual(emprestimo.getMaxReturnDate());
    }

    @Override
    public boolean wasReturnedLate(Emprestimo emprestimo){
        return emprestimo.getReturnDate().isAfter(emprestimo.getMaxReturnDate());
    }
}
