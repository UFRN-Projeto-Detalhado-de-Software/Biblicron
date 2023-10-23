package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.model.User;
import edu.ufrn.imd.Biblicron.repository.IEmprestimoRepository;
import edu.ufrn.imd.Biblicron.repository.ILivroRepository;
import edu.ufrn.imd.Biblicron.repository.IUserRepository;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {
    final IEmprestimoRepository emprestimoRepository;
    final ILivroRepository livroRepository;
    final IUserRepository userRepository;

    public EmprestimoService(IEmprestimoRepository emprestimoRepository, ILivroRepository livroRepository, IUserRepository userRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.userRepository = userRepository;
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
    public Emprestimo realizarEmprestimo(String nomeLivro, String nomeUsuario) {

        ArrayList<String> errosLog = new ArrayList<>();

        Optional<Livro> livro = livroRepository.findByTitulo(nomeLivro);
        if(livro.isEmpty()){
            errosLog.add("Not Found: Livro não encontrado com o título: " + nomeLivro);
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

        if(livro.isPresent() && emprestimoEmAndamento.isEmpty() && usuario.isPresent()) {
            // Verifica se há livros disponíveis no estoque
            if (livro.get().getQuantidadeDisponivel() > 0) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setLivro(livro.get());
                emprestimo.setUsuario(usuario.get());
                emprestimo.setLoanDate(LocalDate.now());
                emprestimo.setMaxReturnDate(LocalDate.now().plusDays(15)); //seta data de devolução 15 dias à frente
                emprestimo.setReturnDate(null);

                // Atualiza a quantidade disponível no estoque
                livro.get().setQuantidadeDisponivel(livro.get().getQuantidadeDisponivel() - 1);
                livroRepository.save(livro.get());

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
            if (emprestimo.getReturnDate() == null) {
                // Atualize a data de devolução para a data atual
                emprestimo.setReturnDate(LocalDate.now());

                // Recupere o livro associado ao empréstimo
                Livro livro = emprestimo.getLivro();

                // Incrementar a quantidade disponível no estoque
                livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);

                // Salve as alterações no banco de dados
                livroRepository.save(livro);

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
            throw new IllegalStateException(String.valueOf(errosLog));
        }
        return null;
    }

    @Transactional
    public Emprestimo estenderVencimento(Long id) {
        ArrayList<String> errosLog = new ArrayList<>();
        Optional<Emprestimo> emprestimoOptional = emprestimoRepository.findById(id);

        if (emprestimoOptional.isPresent()) {
            Emprestimo emprestimo = emprestimoOptional.get();

            if(emprestimo.getReturnDate() != null){
                errosLog.add("Conflict: Devolução já realizada para empréstimo de ID: " + id);
            }
            else{
                if (LocalDate.now().isBefore(emprestimo.getMaxReturnDate()) || LocalDate.now().isEqual(emprestimo.getMaxReturnDate())){
                    // Soma mais 15 dias à Data máxima de devolução
                    emprestimo.setMaxReturnDate(emprestimo.getMaxReturnDate().plusDays(15));
                    return emprestimoRepository.save(emprestimo);
                }
                else{
                    errosLog.add("Conflict: Empréstimo já vencido.");
                }
            }
        }
        else{
            errosLog.add("Not Found: Empréstimo não encontrado com o ID: " + id);
        }
        if(!errosLog.isEmpty()){
            throw new IllegalStateException(String.valueOf(errosLog));
        }
        return null;
    }

    public List<Emprestimo> findEmprestimosComMaxReturnDate(LocalDate dataLimite) {
        return emprestimoRepository.findByMaxReturnDateWithin24Hours(dataLimite, dataLimite.plusDays(1));
    }
}
