package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.DispositivoEletronico;
import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.repository.IDispositivoEletronicoRepository;
import edu.ufrn.imd.Biblicron.repository.IEmprestimoRepository;
import edu.ufrn.imd.Biblicron.repository.IProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DispositivoEletronicoService extends ProdutoService<DispositivoEletronico>{

    final IDispositivoEletronicoRepository dispositivoEletronicoRepository;
    final IEmprestimoRepository emprestimoRepository;
    final IProdutoRepository produtoRepository;

    public DispositivoEletronicoService(IDispositivoEletronicoRepository dispositivoEletronicoRepository, IEmprestimoRepository emprestimoRepository, IProdutoRepository produtoRepository) {
        this.dispositivoEletronicoRepository = dispositivoEletronicoRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public DispositivoEletronico save(DispositivoEletronico dispositivoEletronico){
        ArrayList<String> errorsLog = new ArrayList<>();

        //Validação utilizando método de ProdutoService
        errorsLog.addAll(validateCreation(dispositivoEletronico));

        //Checagem de Serial Number Único
        Optional<DispositivoEletronico> dispositivoEletronicoOptional = dispositivoEletronicoRepository.findBySerialNumber(dispositivoEletronico.getSerialNumber());
        if(dispositivoEletronicoOptional.isPresent()){
            errorsLog.add("Conflict: Serial Number já cadastrado");
        }

        if(dispositivoEletronico.getSerialNumber() != null && dispositivoEletronico.getSerialNumber().length() > 255){
            errorsLog.add("Length Required: Serial Number precisa ter menos de 255 caracteres");
        }
        if(dispositivoEletronico.getModelo() != null && dispositivoEletronico.getModelo().length() > 255){
            errorsLog.add("Length Required: Modelo precisa ter menos de 255 caracteres");
        }
        if(dispositivoEletronico.getMarca() != null && dispositivoEletronico.getMarca().length() > 255){
            errorsLog.add("Length Required: Marca precisa ter menos de 255 caracteres");
        }

        if(!errorsLog.isEmpty()){
            throw new IllegalStateException(String.join("\n", errorsLog));
        }

        return dispositivoEletronicoRepository.save(dispositivoEletronico);
    }

    public Optional<DispositivoEletronico> findById(Long id){
        return dispositivoEletronicoRepository.findById(id);
    }

    public Page<DispositivoEletronico> findAll(Pageable pageable){
        return dispositivoEletronicoRepository.findAll(pageable);
    }

    @Transactional
    public void delete(DispositivoEletronico dispositivoEletronico){
        List<Emprestimo> currentUsage = emprestimoRepository.findByProdutoAndReturnDateIsNull(dispositivoEletronico);
        List<Emprestimo> isAtLoanLog = emprestimoRepository.findByProduto(dispositivoEletronico);
        if(!(currentUsage.isEmpty())){
            throw new IllegalStateException("Conflict: Produto " + dispositivoEletronico.getNomeProduto() + " está emprestado");
        }
        if(!(isAtLoanLog.isEmpty())){
            throw new IllegalStateException("Conflict: Produto " + dispositivoEletronico.getNomeProduto() + " está nos registros de empréstimo e não pode ser apagado");
        }
        dispositivoEletronicoRepository.delete(dispositivoEletronico);
    }

    @Transactional
    public DispositivoEletronico update(DispositivoEletronico dispositivoEletronico){
        ArrayList<String> errorsLog = new ArrayList<>();

        Optional<DispositivoEletronico> dispositivoEletronicoByNome = dispositivoEletronicoRepository.findByNomeProduto(dispositivoEletronico.getNomeProduto());
        Optional<DispositivoEletronico> dispositivoEletronicobySerialNumber = dispositivoEletronicoRepository.findBySerialNumber(dispositivoEletronico.getSerialNumber());

        if(dispositivoEletronicoByNome.isPresent() && dispositivoEletronicoByNome.get().getId() != dispositivoEletronico.getId()){
            errorsLog.add("Conflict: Nome de produto já está em uso");
        }

        if(dispositivoEletronicobySerialNumber.isPresent() && dispositivoEletronicobySerialNumber.get().getId() != dispositivoEletronico.getId()){
            errorsLog.add("Conflict: Serial Number já está em uso");
        }

        Optional<DispositivoEletronico> dispositivoEletronicoOptional = dispositivoEletronicoRepository.findById(dispositivoEletronico.getId());
        if(!dispositivoEletronicoOptional.isPresent()){
            errorsLog.add("Not Found: Book not found.");
        }

        errorsLog.addAll(validateValues(dispositivoEletronico));

        if(dispositivoEletronico.getSerialNumber() != null && dispositivoEletronico.getSerialNumber().length() > 255){
            errorsLog.add("Length Required: Serial Number precisa ter menos de 255 caracteres");
        }
        if(dispositivoEletronico.getModelo() != null && dispositivoEletronico.getModelo().length() > 255){
            errorsLog.add("Length Required: Modelo precisa ter menos de 255 caracteres");
        }
        if(dispositivoEletronico.getMarca() != null && dispositivoEletronico.getMarca().length() > 255){
            errorsLog.add("Length Required: Marca precisa ter menos de 255 caracteres");
        }

        if(!errorsLog.isEmpty()){
            throw new IllegalStateException(String.join("\n", errorsLog));
        }

        return dispositivoEletronicoRepository.save(dispositivoEletronico);
    }


}
