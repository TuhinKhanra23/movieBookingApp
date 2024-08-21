package com.cts.fse.repository;

import com.cts.fse.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface MovieRepo extends MongoRepository<Movie,String> {
    List<Movie> findByMovieName(String movieName);
}
