package edu.ufrn.imd.Biblicron.strategies.models;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.strategies.interfaces.IDevolucaoStrategy;

import java.time.LocalDate;

public class DevolucaoDispositivoEletronicoStrategy implements IDevolucaoStrategy {

    @Override
    public boolean canBeReturned(Emprestimo emprestimo) {
        return emprestimo.getReturnDate() == null && !LocalDate.now().isAfter(emprestimo.getMaxReturnDate().plusDays(1));
    }

    @Override
    public boolean isReturned(Emprestimo emprestimo) {
        return emprestimo.getReturnDate() != null;
    }

    @Override
    public boolean canBeExtended(Emprestimo emprestimo) {
        return LocalDate.now().isBefore(emprestimo.getMaxReturnDate().plusDays(1)) || LocalDate.now().isEqual(emprestimo.getMaxReturnDate().plusDays(1));
    }

    @Override
    public boolean isLate(Emprestimo emprestimo) {
        return emprestimo.getMaxReturnDate().isBefore(LocalDate.now().plusDays(1));
    }

    @Override
    public boolean wasReturnedLate(Emprestimo emprestimo) {
        return emprestimo.getReturnDate().isAfter(emprestimo.getMaxReturnDate().plusDays(1));
    }
}
