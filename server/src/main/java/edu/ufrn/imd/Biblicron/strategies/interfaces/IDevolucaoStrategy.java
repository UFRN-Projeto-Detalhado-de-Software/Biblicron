package edu.ufrn.imd.Biblicron.strategies.interfaces;

import edu.ufrn.imd.Biblicron.model.Emprestimo;


public interface IDevolucaoStrategy {

    public abstract boolean canBeReturned(Emprestimo emprestimo);
    public abstract boolean canBeExtended(Emprestimo emprestimo);
    public abstract boolean isLate(Emprestimo emprestimo);
    public abstract boolean isReturned(Emprestimo emprestimo);
    public abstract boolean wasReturnedLate(Emprestimo emprestimo);

}
