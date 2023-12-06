package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.MaterialEsportivo;
import edu.ufrn.imd.Biblicron.repository.IMaterialEsportivoRepository;
import edu.ufrn.imd.Biblicron.repository.IProdutoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class MaterialEsportivoService extends ProdutoService<MaterialEsportivo> {
    final IMaterialEsportivoRepository materialEsportivoRepository;
    final IProdutoRepository produtoRepository;

    public MaterialEsportivoService(IMaterialEsportivoRepository materialEsportivoRepository, IProdutoRepository produtoRepository) {
        this.materialEsportivoRepository = materialEsportivoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public MaterialEsportivo save(MaterialEsportivo materialEsportivo){
        ArrayList<String> errorsLog = new ArrayList<>();

        errorsLog.addAll(validate(materialEsportivo));

        if(materialEsportivo.getCategoria() == null){
            errorsLog.add("Length required: Material esportivo precisa ter uma Categoria.");
        }

        if(materialEsportivo.getTamanhos() != null && materialEsportivo.getTamanhos().size() > 5){
            errorsLog.add("Length required: Material esportivo precisa ter, no máximo, 5 tamanhos.");
        }
        if(materialEsportivo.getMarca() == null){
            errorsLog.add("Length required: Material esportivo precisa ter uma marca");
        }
        if(materialEsportivo.getMarca().length() > 255){
            errorsLog.add("Length required: Marca precisa ter menos de 255 caracteres");
        }

        if(!errorsLog.isEmpty()){
            throw new IllegalStateException(String.join("\n", errorsLog));
        }

        return materialEsportivoRepository.save(materialEsportivo);
    }


}
