package com.cts.fse.repository;

import com.cts.fse.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface TicketRepo extends MongoRepository<Ticket, String> {
    List<Ticket> findByUserId(String userId);
}
