package edu.ufrn.imd.Biblicron.factory;

import edu.ufrn.imd.Biblicron.factory.interfaces.IStrategyFactory;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.model.MaterialEsportivo;
import edu.ufrn.imd.Biblicron.model.Produto;
import edu.ufrn.imd.Biblicron.strategies.interfaces.ICalculoFinalStrategy;
import edu.ufrn.imd.Biblicron.strategies.interfaces.IDevolucaoStrategy;
import edu.ufrn.imd.Biblicron.strategies.models.CalculoFinalLivroStrategy;
import edu.ufrn.imd.Biblicron.strategies.models.DevolucaoLivroStrategy;
import org.springframework.stereotype.Component;

@Component
public class StrategyFactory implements IStrategyFactory {
    @Override
    public IDevolucaoStrategy getDevolucaoStrategy(Produto produto){
        if(produto instanceof Livro){
            System.out.println("New instancia DevolucaoLivroStrategy");

            return new DevolucaoLivroStrategy();
        }
        else if(produto instanceof MaterialEsportivo){
            System.out.println("New instancia DevolucaoMaterialEsportivo");
            return new DevolucaoLivroStrategy();
        }
        throw new IllegalArgumentException("Estratégia de devolução não encontrada para o tipo de produto");
    }

    @Override
    public ICalculoFinalStrategy getCalculoFinalStrategy(Produto produto){
        if(produto instanceof Livro){
            System.out.println("New instancia CalculoFinalLivro");
            return new CalculoFinalLivroStrategy();
        }
        else if (produto instanceof MaterialEsportivo){
            System.out.println("New instancia CalculoFinalMaterialEsportivo");
            return new CalculoFinalLivroStrategy();
        }
        throw new IllegalArgumentException("Estratégia de cálculo final não encontrada para o tipo de produto");
    }
}
