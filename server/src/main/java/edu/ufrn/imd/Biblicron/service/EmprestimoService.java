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
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
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

        Livro livro = livroRepository.findByTitulo(nomeLivro)
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado com o nome: " + nomeLivro));

        User usuario = userRepository.findByUsername(nomeUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o nome: " + nomeUsuario));

        // Verifica se o usuário já possui um empréstimo em andamento
        Optional<Emprestimo> emprestimoEmAndamento = emprestimoRepository.findByUsuarioAndReturnDateIsNull(usuario);

        if (emprestimoEmAndamento.isPresent()) {
            throw new IllegalStateException("Usuário já possui um empréstimo em andamento.");
        }

        // Verifica se há livros disponíveis no estoque
        if (livro.getQuantidade() > 0) {
            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setLivro(livro);
            emprestimo.setUsuario(usuario);
            emprestimo.setLoanDate(LocalDate.now());
            emprestimo.setMaxReturnDate(LocalDate.now().plusDays(15)); //seta data de devolução 15 dias à frente
            emprestimo.setReturnDate(null);

            // Atualiza a quantidade disponível no estoque
            livro.setQuantidade(livro.getQuantidade() - 1);
            livroRepository.save(livro);

            // Salve o empréstimo no banco de dados
            return emprestimoRepository.save(emprestimo);
        } else {
            throw new IllegalStateException("Livro não disponível no estoque para empréstimo");
        }
    }


    @Transactional
    public Emprestimo realizarDevolucao(Long id) {
        Optional<Emprestimo> emprestimoOptional = emprestimoRepository.findById(id);

        if (emprestimoOptional.isPresent()) {
            Emprestimo emprestimo = emprestimoOptional.get();
            if (emprestimo.getReturnDate() == null) {
                // Atualize a data de devolução para a data atual
                emprestimo.setReturnDate(LocalDate.now());

                // Recupere o livro associado ao empréstimo
                Livro livro = emprestimo.getLivro();

                // Incrementar a quantidade disponível no estoque
                livro.setQuantidade(livro.getQuantidade() + 1);

                // Salve as alterações no banco de dados
                livroRepository.save(livro);
                emprestimoRepository.save(emprestimo);

                return emprestimo;
            }
        }

        throw new EntityNotFoundException("Empréstimo não encontrado com o ID: " + id);
    }

    @Transactional
    public Emprestimo estenderVencimento(Long id) {
        Optional<Emprestimo> emprestimoOptional = emprestimoRepository.findById(id);

        if (emprestimoOptional.isPresent()) {
            Emprestimo emprestimo = emprestimoOptional.get();

            if(emprestimo.getReturnDate() != null){
                throw new IllegalStateException("Devolução já realizada para empréstimo de ID: " + id);
            }

            if (LocalDate.now().isBefore(emprestimo.getMaxReturnDate())) {
                // Soma mais 15 dias à Data máxima de devolução
                emprestimo.setMaxReturnDate(emprestimo.getMaxReturnDate().plusDays(15));

                emprestimoRepository.save(emprestimo);

                return emprestimo;
            }
        }

        throw new EntityNotFoundException("Empréstimo não encontrado com o ID: " + id);
    }

    public List<Emprestimo> findEmprestimosComMaxReturnDate(LocalDate dataLimite) {
        return emprestimoRepository.findByMaxReturnDateWithin24Hours(dataLimite, dataLimite.plusDays(1));
    }
}
