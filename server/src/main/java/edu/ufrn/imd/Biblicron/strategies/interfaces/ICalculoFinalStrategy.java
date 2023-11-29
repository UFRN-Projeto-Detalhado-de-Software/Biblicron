package edu.ufrn.imd.Biblicron.strategies.interfaces;

import edu.ufrn.imd.Biblicron.model.Emprestimo;

public interface ICalculoFinalStrategy {
    public abstract float calculateFinalValueLate(Emprestimo emprestimo);
    public abstract float calculateFinalValueRegular(Emprestimo emprestimo);
}
