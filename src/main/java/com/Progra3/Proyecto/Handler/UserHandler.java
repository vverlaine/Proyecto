package com.Progra3.Proyecto.Handler;

import com.Progra3.Proyecto.Model.User;
import com.Progra3.Proyecto.Repository.UserRepository;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserHandler {

    private UserRepository userRepository;

    public UserHandler(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    public Mono<ServerResponse> getUserAll(ServerRequest request){
        Flux<User> user=userRepository.findAll();
        return ServerResponse.ok().body(user,User.class);
    }


}
