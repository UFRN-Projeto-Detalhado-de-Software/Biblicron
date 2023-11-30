package edu.ufrn.imd.Biblicron.strategies.models;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.strategies.interfaces.ICalculoFinalStrategy;
import edu.ufrn.imd.Biblicron.strategies.interfaces.IDevolucaoStrategy;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class CalculoFinalLivroStrategy implements ICalculoFinalStrategy {

    @Override
    public float calculateFinalValueLate(Emprestimo emprestimo) {
        long regularLoanedDays = ChronoUnit.DAYS.between(emprestimo.getLoanDate(), emprestimo.getMaxReturnDate());
        long amountOfLateDays = ChronoUnit.DAYS.between(emprestimo.getMaxReturnDate(), emprestimo.getReturnDate());
        return (regularLoanedDays * (emprestimo.getLivro().getValor() * 0.02f)) + (amountOfLateDays * (emprestimo.getLivro().getValor() * 0.04f));
    }

    @Override
    public float calculateFinalValueRegular(Emprestimo emprestimo) {
        long amountOfLoanedDays = ChronoUnit.DAYS.between(emprestimo.getLoanDate(), emprestimo.getReturnDate());

        return (amountOfLoanedDays * (emprestimo.getLivro().getValor() * 0.02f));
    }
}
