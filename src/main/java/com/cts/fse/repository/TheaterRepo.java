package com.cts.fse.repository;

import com.cts.fse.model.Theater;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepo extends MongoRepository<Theater, Integer> {
    List<Theater> findByTheaterNameAndTheaterLoc(String theaterName, String theaterLoc);
}
