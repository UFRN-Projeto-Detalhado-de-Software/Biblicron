package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.Produto;
import edu.ufrn.imd.Biblicron.repository.IProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public abstract class ProdutoService<T extends Produto> {

    @Autowired
    IProdutoRepository produtoRepository;


    protected ArrayList<String> validate(T produto) {
        ArrayList<String> errorsLog = new ArrayList<>();

        if(produto.getQuantidade() <= 0){
            errorsLog.add("Length Required: Quantidade de Produtos precisa ser maior do que 0.");
        }
        if(produto.getQuantidadeDisponivel() <= 0){
            errorsLog.add("Length Required: Quantidade Disponível de Produtos precisa ser maior do que 0.");
        }
        if(produto.getQuantidade() < produto.getQuantidadeDisponivel()){
            errorsLog.add("Conflict: Quantidade Disponível precisa ser menor ou igual à Quantidade Total.");
        }
        if(produto.getValor() <= 0){
            errorsLog.add("Length Required: Valor do produto precisar ser maior que 0.");
        }
        if(produtoRepository.existsByNomeProduto(produto.getNomeProduto())){
            errorsLog.add("Conflict: Produto já cadastrado com este nome.");
        }

        return errorsLog;
    }

}




