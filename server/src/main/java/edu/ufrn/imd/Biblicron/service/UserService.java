package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.Emprestimo;
import edu.ufrn.imd.Biblicron.model.Enum.Genero;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.model.User;
import edu.ufrn.imd.Biblicron.repository.IEmprestimoRepository;
import edu.ufrn.imd.Biblicron.repository.IUserRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    final IUserRepository userRepository;
    final IEmprestimoRepository emprestimoRepository;

    public UserService(IUserRepository userRepository, IEmprestimoRepository emprestimoRepository) {
        this.userRepository = userRepository;
        this.emprestimoRepository = emprestimoRepository;
    }


    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        ArrayList<String> errosLog = new ArrayList<>();

        if(existsByUsername(user.getUsername())){
            errosLog.add("Conflict: Username already in use.");
        }
        if(existsByEmail(user.getEmail())){
            errosLog.add("Conflict: Email already in use.");
        }
        if(user.getUsername().length() > 50){
            errosLog.add("Length Required: Username must have less than 50 characters.");
        }
        if(user.getPassword().length() > 50){
            errosLog.add("Length Required: Password must have less than 50 characters.");
        }
        if(user.getEmail().length() > 50){
            errosLog.add("Length Required: Email must have less than 50 characters.");
        }
        if (user.getUserType() == null) {
            errosLog.add("Length Required: O campo 'userType' não pode ser nulo.");
        }

        if(!errosLog.isEmpty()){
            throw new IllegalStateException(String.valueOf(errosLog));
        }

        return userRepository.save(user);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void delete(User user) {
        Optional<Emprestimo> currentLoan = emprestimoRepository.findByUsuarioAndReturnDateIsNull(user);
        List<Emprestimo> loanRecords = emprestimoRepository.findByUsuario(user);
        if(currentLoan.isPresent()){
            throw new IllegalStateException("Conflict: User " + user.getUsername() + " tem um empréstimo em andamento");
        }
        if(!loanRecords.isEmpty()){
            throw new IllegalStateException("Conflict: User " + user.getUsername() + " está nos registros de empréstimo e não pode ser apagado");
        }
        userRepository.delete(user);
    }

    @Transactional
    public User update(User user){
        ArrayList<String> errosLog = new ArrayList<>();

        Optional<User> userByUsername = findByUsername(user.getUsername());
        if(userByUsername.isPresent() && userByUsername.get().getId() != user.getId() ){
            errosLog.add("Conflict: Username is already in use.");
        }

        Optional<User> userByEmail = findByEmail(user.getEmail());
        if(userByEmail.isPresent() && userByEmail.get().getId() != user.getId()){
            errosLog.add("Conflict: Email is already in user.");
        }

        Optional<User> userOptional = findById(user.getId());
        if(!userOptional.isPresent()){
            errosLog.add("Not found: User not found");
        }

        if(user.getUsername().length() > 50){
            errosLog.add("Length Required: Username must have less than 50 characters.");
        }
        if(user.getPassword().length() > 50){
            errosLog.add("Length Required: Password must have less than 50 characters.");
        }
        if(user.getEmail().length() > 50){
            errosLog.add("Length Required: Email must have less than 50 characters.");
        }
        if (user.getUserType() == null) {
            errosLog.add("Length Required: O campo 'userType' não pode ser nulo.");
        }

        if(!errosLog.isEmpty()){
            throw new IllegalStateException(String.valueOf(errosLog));
        }
        return userRepository.save(user);
    }

    public User login(String userName, String password) {
        ArrayList<String> errosLog = new ArrayList<>();
        if(userName == null){
            errosLog.add("Empty: Necessário Informar o campo de UserName.");
        }
        if(password == null){
            errosLog.add("Empty: Necessário Informar o campo de Password.");
        }
        if(userName.length() > 50){
            errosLog.add("Length Required: Username must have less than 50 characters.");
        }
        if(password.length() > 50){
            errosLog.add("Length Required: Password must have less than 50 characters.");
        }

        Optional<User> userOptional = userRepository.findByUsernameAndPassword(userName, password);
        var user = new User();
        if(userOptional.isPresent()) {
            try {
                BeanUtils.copyProperties(user, userOptional.get());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            user.setId(userOptional.get().getId());
        }

        if(!userOptional.isPresent()){
            errosLog.add("Not Found: Não foi possível encontrar um usuário com essas credenciais.");
        }

        if(!errosLog.isEmpty()){
            throw new IllegalStateException(String.valueOf(errosLog));
        }
        return user;
    }

    public List<Livro> generateSugestoesById(Long id){
        var usuario = userRepository.findById(id).get();

        var emprestimos = emprestimoRepository.findEmprestimosByUsuario(id);

        var livrosPercorridos = new HashMap<Livro, Boolean>();


        var generos = new HashMap<Genero, Integer>();

        for (Emprestimo emprestimo : emprestimos) {
            var livro = emprestimo.getLivro();
            if (livrosPercorridos.containsKey(usuario)) {
                continue;
            }
            livrosPercorridos.put(livro, true);
            var livroGeneros = livro.getGeneros();

            for(var genero : livroGeneros) {
                generos.put(genero, generos.getOrDefault(genero, 0) + 1);
            }
        }

        var generoMaisEmprestado = generos.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).collect(Collectors.toList()).get(0).getKey();

        var emprestimosDoGeneroMaisEmprestado = emprestimoRepository.findEmprestimosByLivroGenero(generoMaisEmprestado);

        var livrosRecomendados = new HashMap<Livro, Integer>();

        for (var emprestimo : emprestimosDoGeneroMaisEmprestado) {
            var livroRecomendado = emprestimo.getLivro();
            livrosRecomendados.put(livroRecomendado, livrosRecomendados.getOrDefault(livroRecomendado, 0) + 1);
        }

        var sortedSugestions = livrosRecomendados.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toList());

        return sortedSugestions.stream()
                .map(sugestao -> sugestao.getKey())
                .collect(Collectors.toList());
    }
}
