package edu.ufrn.imd.Biblicron.strategies.models;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.strategies.interfaces.ICalculoFinalStrategy;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class CalculoFinalMaterialEsportivoStrategy implements ICalculoFinalStrategy {
    @Override
    public float calculateFinalValueLate(Emprestimo emprestimo) {
        long regularLoanedDays = ChronoUnit.DAYS.between(emprestimo.getLoanDate(), emprestimo.getMaxReturnDate().plusDays(5));
        long amountOfLateDays = ChronoUnit.DAYS.between(emprestimo.getMaxReturnDate().plusDays(5), emprestimo.getReturnDate());
        return (regularLoanedDays * (emprestimo.getProduto().getValor() * 0.04f)) + (amountOfLateDays * (emprestimo.getProduto().getValor() * 0.08f));
    }

    @Override
    public float calculateFinalValueRegular(Emprestimo emprestimo) {
        long amountOfLoanedDays = ChronoUnit.DAYS.between(emprestimo.getLoanDate(), emprestimo.getReturnDate());
        return (amountOfLoanedDays * (emprestimo.getProduto().getValor() * 0.04f));
    }
}
