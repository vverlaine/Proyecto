package com.Progra3.Proyecto.Repository;

import com.Progra3.Proyecto.Model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {


}
