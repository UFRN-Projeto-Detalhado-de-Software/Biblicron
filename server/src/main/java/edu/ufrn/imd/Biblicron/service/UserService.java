package edu.ufrn.imd.Biblicron.service;

import edu.ufrn.imd.Biblicron.model.User;
import edu.ufrn.imd.Biblicron.repository.IUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        if(existsByUsername(user.getUsername())){
            throw new IllegalStateException("Conflict: Username already in use.");
        }
        if(existsByEmail(user.getEmail())){
            throw new IllegalStateException("Conflict: Email already in use");
        }
        if(user.getUsername().length() > 50){
            throw new IllegalArgumentException("Length Required: Username must have less than 50 characters.");
        }
        if(user.getPassword().length() > 50){
            throw new IllegalArgumentException("Length Required: Password must have less than 50 characters.");
        }
        if(user.getEmail().length() > 50){
            throw new IllegalArgumentException("Length Required: Email must have less than 50 characters.");
        }
        if (user.getUserType() == null) {
            throw new IllegalArgumentException("Length Required: O campo 'userType' não pode ser nulo.");
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


    public void delete(User user) {
        userRepository.delete(user);
    }
}
