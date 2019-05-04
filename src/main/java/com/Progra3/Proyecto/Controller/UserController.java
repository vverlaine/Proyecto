package com.Progra3.Proyecto.Controller;

import com.Progra3.Proyecto.Model.User;
import com.Progra3.Proyecto.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public Flux<User> getUser() {
        return userRepository.findAll();
    }

    @PostMapping("/create")
    public Mono<User> addUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> getUserID(@PathVariable(value = "id") String userId) {
        return userRepository.findById(userId)
                .map(savedUser -> ResponseEntity.ok(savedUser))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //Server Sent Events
    @GetMapping(value = "/stream/user", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamUserAll() {
        return userRepository.findAll().delayElements(Duration.ofMillis(1));
    }

    @PutMapping("/user/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable(value = "id") String userId,
                                                 @Valid @RequestBody User user) {
        return userRepository.findById(userId)
                .flatMap(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setRol(user.getRol());
                    return userRepository.save(existingUser);
                }).map(updatedUser -> new ResponseEntity<>(updatedUser, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/user/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable(value = "id") String userId) {
        return userRepository.findById(userId)
                .flatMap(existingUser ->
                        userRepository.delete(existingUser))
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.ACCEPTED)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
