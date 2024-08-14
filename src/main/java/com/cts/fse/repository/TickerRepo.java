package com.cts.fse.repository;

import com.cts.fse.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface TickerRepo extends MongoRepository<Ticket,String> {
}
