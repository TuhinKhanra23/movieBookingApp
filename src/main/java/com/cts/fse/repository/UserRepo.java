package com.cts.fse.repository;

import com.cts.fse.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepo extends MongoRepository<User, String> {

}
