package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.model.MaterialEsportivo;
import edu.ufrn.imd.Biblicron.repository.IEmprestimoRepository;
import edu.ufrn.imd.Biblicron.repository.IMaterialEsportivoRepository;
import edu.ufrn.imd.Biblicron.repository.IProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialEsportivoService extends ProdutoService<MaterialEsportivo> {
    final IMaterialEsportivoRepository materialEsportivoRepository;
    final IEmprestimoRepository emprestimoRepository;
    final IProdutoRepository produtoRepository;

    public MaterialEsportivoService(IMaterialEsportivoRepository materialEsportivoRepository, IEmprestimoRepository emprestimoRepository, IProdutoRepository produtoRepository) {
        this.materialEsportivoRepository = materialEsportivoRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public MaterialEsportivo save(MaterialEsportivo materialEsportivo){
        ArrayList<String> errorsLog = new ArrayList<>();

        errorsLog.addAll(validateCreation(materialEsportivo));

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

    public Page<MaterialEsportivo> findAll(Pageable pageable){
        return materialEsportivoRepository.findAll(pageable);
    }

    public Optional<MaterialEsportivo> findById(Long id){
        return materialEsportivoRepository.findById(id);
    }

    @Transactional
    public void delete(MaterialEsportivo materialEsportivo){
        List<Emprestimo> currentUsage = emprestimoRepository.findByProdutoAndReturnDateIsNull(materialEsportivo);
        List<Emprestimo> isAtLoanLog = emprestimoRepository.findByProduto(materialEsportivo);
        if(!(currentUsage.isEmpty())){
            throw new IllegalStateException("Conflict: Produto " + materialEsportivo.getNomeProduto() + " está emprestado");
        }
        if(!(isAtLoanLog.isEmpty())){
            throw new IllegalStateException("Conflict: Produto " + materialEsportivo.getNomeProduto() + " está nos registros de empréstimo e não pode ser apagado");
        }
        materialEsportivoRepository.delete(materialEsportivo);
    }

    @Transactional
    public MaterialEsportivo update(MaterialEsportivo materialEsportivo){
        ArrayList<String> errorsLog = new ArrayList<>();

        Optional<MaterialEsportivo> materialEsportivoByNome = materialEsportivoRepository.findByNomeProduto(materialEsportivo.getNomeProduto());

        if(materialEsportivoByNome.isPresent() && materialEsportivoByNome.get().getId() != materialEsportivo.getId()){
            errorsLog.add("Conflict: Nome de produto já está em uso");
        }

        Optional<MaterialEsportivo> materialEsportivoOptional = materialEsportivoRepository.findById(materialEsportivo.getId());
        if(!materialEsportivoOptional.isPresent()){
            errorsLog.add("Not Found: Book not found.");
        }

        errorsLog.addAll(validateValues(materialEsportivo));

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
