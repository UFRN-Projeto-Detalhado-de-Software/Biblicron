package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.factory.interfaces.IStrategyFactory;
import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.model.Produto;
import edu.ufrn.imd.Biblicron.model.User;
import edu.ufrn.imd.Biblicron.repository.IEmprestimoRepository;
import edu.ufrn.imd.Biblicron.repository.ILivroRepository;
import edu.ufrn.imd.Biblicron.repository.IProdutoRepository;
import edu.ufrn.imd.Biblicron.repository.IUserRepository;
import edu.ufrn.imd.Biblicron.strategies.interfaces.ICalculoFinalStrategy;
import edu.ufrn.imd.Biblicron.strategies.interfaces.IDevolucaoStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {
    private final IEmprestimoRepository emprestimoRepository;
    private final IProdutoRepository produtoRepository;
    private final IUserRepository userRepository;
    private final IStrategyFactory strategyFactory;

    public EmprestimoService(IEmprestimoRepository emprestimoRepository, ILivroRepository livroRepository, IProdutoRepository produtoRepository, IUserRepository userRepository, IStrategyFactory strategyFactory) {
        this.emprestimoRepository = emprestimoRepository;
        this.produtoRepository = produtoRepository;
        this.userRepository = userRepository;
        this.strategyFactory = strategyFactory;
    }

    @Transactional
    public Emprestimo save(Emprestimo loan){
        return emprestimoRepository.save(loan);
    }

    public Page<Emprestimo> findAll(Pageable pageable){
        return emprestimoRepository.findAll(pageable);
    }

    public Optional<Emprestimo> findById(Long id){
        return emprestimoRepository.findById(id);
    }

    @Transactional
    public Emprestimo realizarEmprestimo(String nomeProduto, String nomeUsuario) {

        ArrayList<String> errosLog = new ArrayList<>();

        Optional<Produto> produto = produtoRepository.findByNomeProduto(nomeProduto);
        if(produto.isEmpty()){
            errosLog.add("Not Found: Livro não encontrado com o título: " + nomeProduto);
        }

        Optional<User> usuario = userRepository.findByUsername(nomeUsuario);
        if(usuario.isEmpty()){
            errosLog.add("Not Found: Usuário não encontrado com o nome: " + nomeUsuario);
        }

        Optional<Emprestimo> emprestimoEmAndamento = Optional.empty();
        // Verifica se o usuário já possui um empréstimo em andamento
        if(usuario.isPresent()) {
            emprestimoEmAndamento = emprestimoRepository.findByUsuarioAndReturnDateIsNull(usuario.get());
        }

        if (emprestimoEmAndamento.isPresent()) {
            errosLog.add("Conflict: Usuário já possui um empréstimo em andamento.");
        }

        if(produto.isPresent() && emprestimoEmAndamento.isEmpty() && usuario.isPresent()) {
            // Verifica se há livros disponíveis no estoque
            if (produto.get().getQuantidadeDisponivel() > 0) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setProduto(produto.get());
                emprestimo.setUsuario(usuario.get());
                emprestimo.setLoanDate(LocalDate.now());
                emprestimo.setMaxReturnDate(LocalDate.now().plusDays(15)); //seta data de devolução 15 dias à frente
                emprestimo.setReturnDate(null);

                // Atualiza a quantidade disponível no estoque
                produto.get().setQuantidadeDisponivel(produto.get().getQuantidadeDisponivel() - 1);
                produtoRepository.save(produto.get());

                // Salve o empréstimo no banco de dados
                return emprestimoRepository.save(emprestimo);
            } else {
                errosLog.add("Conflict: Livro não disponível no estoque para empréstimo");
            }
        }

        if(!errosLog.isEmpty()){
            throw new IllegalStateException(String.valueOf(errosLog));
        }
        return null;
    }


    @Transactional
    public Emprestimo realizarDevolucao(Long id) {
        ArrayList<String> errosLog = new ArrayList<>();
        Optional<Emprestimo> emprestimoOptional = emprestimoRepository.findById(id);

        if (emprestimoOptional.isPresent()) {
            Emprestimo emprestimo = emprestimoOptional.get();
            IDevolucaoStrategy devolucaoStrategy = strategyFactory.getDevolucaoStrategy(emprestimo.getProduto());

            if (devolucaoStrategy.canBeReturned(emprestimo)) {
                // Atualize a data de devolução para a data atual
                emprestimo.setReturnDate(LocalDate.now());

                // Recupere o livro associado ao empréstimo
                Produto produto = emprestimo.getProduto();

                // Incrementar a quantidade disponível no estoque
                produto.setQuantidadeDisponivel(produto.getQuantidadeDisponivel() + 1);

                // Salve as alterações no banco de dados
                produtoRepository.save(produto);

                return emprestimoRepository.save(emprestimo);
            }
            else if(devolucaoStrategy.isLate(emprestimo)){
                emprestimo.setReturnDate(LocalDate.now());
                Produto produto = emprestimo.getProduto();
                produto.setQuantidadeDisponivel(produto.getQuantidadeDisponivel() + 1);
                produtoRepository.save(produto);
                return emprestimoRepository.save(emprestimo);
            }
            else{
                errosLog.add("Conflict: empréstimo já retornado");
            }
        }
        else{
            errosLog.add("Not Found: Empréstimo em andamento não encontrado com o ID: " + id);
        }
        if(!errosLog.isEmpty()){
            throw new IllegalStateException(String.join("\n", errosLog));
        }
        return null;
    }

    @Transactional
    public Emprestimo estenderVencimento(Long id) {
        ArrayList<String> errosLog = new ArrayList<>();
        Optional<Emprestimo> emprestimoOptional = emprestimoRepository.findById(id);

        if (emprestimoOptional.isPresent()) {
            Emprestimo emprestimo = emprestimoOptional.get();
            IDevolucaoStrategy devolucaoStrategy = strategyFactory.getDevolucaoStrategy(emprestimo.getProduto());

            if(devolucaoStrategy.isReturned(emprestimo)){
                errosLog.add("Conflict: Devolução já realizada para empréstimo de ID: " + id);
            }
            else if(devolucaoStrategy.isLate(emprestimo)){
                errosLog.add("Conflict: Empréstimo atrasado");
            }
            else{
                if (devolucaoStrategy.canBeExtended(emprestimo)){
                    // Soma mais 15 dias à Data máxima de devolução
                    emprestimo.setMaxReturnDate(emprestimo.getMaxReturnDate().plusDays(15));
                    return emprestimoRepository.save(emprestimo);
                }
            }
        }
        else{
            errosLog.add("Not Found: Empréstimo não encontrado com o ID: " + id);
        }
        if(!errosLog.isEmpty()){
            throw new IllegalStateException(String.join("\n", errosLog));
        }
        return null;
    }

    @Transactional
    public Emprestimo calcularValorFinal(Long id) {
        ArrayList<String> errosLog = new ArrayList<>();
        Optional<Emprestimo> emprestimoOptional = emprestimoRepository.findById(id);

        if (emprestimoOptional.isPresent()) {
            Emprestimo emprestimo = emprestimoOptional.get();

            IDevolucaoStrategy devolucaoStrategy = strategyFactory.getDevolucaoStrategy(emprestimo.getProduto());
            ICalculoFinalStrategy calculoFinalStrategy = strategyFactory.getCalculoFinalStrategy(emprestimo.getProduto());

            if(devolucaoStrategy.isReturned(emprestimo)){
                if(emprestimo.getValorFinal() == 0) {
                    if (devolucaoStrategy.wasReturnedLate(emprestimo)) {
                        float valorFinal = calculoFinalStrategy.calculateFinalValueLate(emprestimo);
                        emprestimo.setValorFinal(valorFinal);
                        return emprestimoRepository.save(emprestimo);
                    }
                    else {
                        float valorFinal = calculoFinalStrategy.calculateFinalValueRegular(emprestimo);
                        emprestimo.setValorFinal(valorFinal);
                        return emprestimoRepository.save(emprestimo);
                    }
                }
                else{
                    errosLog.add("Conflict: Valor final a ser pago já calculado");
                }
            }
            else{
                errosLog.add("Conflict: Primeiro retorne o empréstimo, para então calcular o valor final");
            }
        }
        else{
            errosLog.add("Not Found: Empréstimo não encontrado com o ID: " + id);
        }
        if(!errosLog.isEmpty()){
            throw new IllegalStateException(String.join("\n", errosLog));
        }
        return null;
    }

    public List<Emprestimo> findEmprestimosComMaxReturnDate(LocalDate dataLimite) {
        return emprestimoRepository.findByMaxReturnDateWithin24Hours(dataLimite, dataLimite.plusDays(1));
    }
}
