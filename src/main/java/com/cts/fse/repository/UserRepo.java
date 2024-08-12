package com.cts.fse.repository;

import com.cts.fse.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UserRepo extends MongoRepository<User, String> {
    List<User> findByEmail(String email);
}
