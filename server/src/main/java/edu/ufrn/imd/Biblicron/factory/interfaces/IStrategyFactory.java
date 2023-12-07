package edu.ufrn.imd.Biblicron.factory.interfaces;

import edu.ufrn.imd.Biblicron.model.Produto;
import edu.ufrn.imd.Biblicron.strategies.interfaces.ICalculoFinalStrategy;
import edu.ufrn.imd.Biblicron.strategies.interfaces.IDevolucaoStrategy;

public interface IStrategyFactory {
    IDevolucaoStrategy getDevolucaoStrategy(Produto produto);
    ICalculoFinalStrategy getCalculoFinalStrategy(Produto produto);
}
