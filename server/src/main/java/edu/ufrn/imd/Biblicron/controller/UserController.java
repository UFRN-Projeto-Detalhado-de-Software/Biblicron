package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.dto.UserDto;
import edu.ufrn.imd.Biblicron.model.Livro;
import edu.ufrn.imd.Biblicron.model.User;
import edu.ufrn.imd.Biblicron.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {
    final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDto userDto){

        var user = new User();
        try {
            BeanUtils.copyProperties(user, userDto);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/listAll")
    public ResponseEntity<Page<User>> findAllUsers(@PageableDefault(page = 0, size = 10, sort = "username", direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable(value = "id")Long id){
        Optional<User> userOptional = userService.findById(id);

        if(!userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id")Long id){
        Optional<User> userOptional = userService.findById(id);
        if(!userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        try{
            userService.delete(userOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Book of id " + id + " deleted successfully.");
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id")Long id,
                                             @RequestBody @Valid UserDto userDto){

        var user = new User();
        try {
            BeanUtils.copyProperties(user, userDto);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        user.setId(id);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.update(user));
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserDto userDto){

        var userName = userDto.getUsername();
        var password = userDto.getPassword();

        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.login(userName, password));
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/sugestoes")
    public ResponseEntity<Object> generateSugestoesById(@PathVariable(value = "id") Long id){
        Optional<User> userOptional = userService.findById(id);
        if(!userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.generateSugestoesById(id));
    }
}
