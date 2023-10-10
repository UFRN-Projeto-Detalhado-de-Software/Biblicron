package edu.ufrn.imd.Biblicron.controller;

import edu.ufrn.imd.Biblicron.dto.UserDto;
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
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<Page<User>> findAllUsers(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable(value = "id")Long id){

        try {
            User user = userService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id")Long id){
        try {
            User user = userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id")Long id,
                                             @RequestBody @Valid UserDto userDto){

        var user = new User();
        try {
            BeanUtils.copyProperties(user, userDto);
            User userAtualizado = this.userService.update(user);
            return ResponseEntity.status(HttpStatus.OK).body(userAtualizado);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
