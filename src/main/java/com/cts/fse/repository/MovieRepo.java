package com.cts.fse.repository;

import com.cts.fse.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface MovieRepo extends MongoRepository<Movie,String> {
}
