package com.cts.fse.service;

import com.cts.fse.config.MongoConnectionConfig;
import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.model.Theater;
import com.cts.fse.repository.TheaterRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class AdminService implements IAdminService {
    @Autowired
    private TheaterRepo theaterRepo;
    @Autowired
    private MongoConnectionConfig mongoConnectionConfig;

    @Override
    public ResponseEntity<String> updateSeatStatus(Integer theaterId) throws MovieBookingException {
        Optional<Theater> theater = theaterRepo.findById(theaterId);
        System.out.println("theater found");
        log.info("Theater Found with theaterId{}", theaterId);

        int availableSeats = 0;
        if (theater.isPresent()) {
            Set<Integer> bookedTickets = theater.get().getBookedTickets();
            availableSeats = theater.get().getTheaterCapacity() - bookedTickets.size();
            Theater theater1 = new Theater();
            theater1.setTheaterId(theater.get().getTheaterId());
            theater1.setBookedTickets(theater.get().getBookedTickets());
            theater1.setTheaterLoc(theater.get().getTheaterLoc());
            theater1.setTheaterName(theater.get().getTheaterName());
            theater1.setTheaterCapacity(theater.get().getTheaterCapacity());
            theater1.setAvailableSeats(availableSeats);
            theaterRepo.save(theater1);
            log.info("Seats Updated Successfully for theaterId{}", theaterId);
            return new ResponseEntity<>("Seats Updated Successfully", HttpStatus.OK);
        } else {
            log.info("Failed to update seat status with theaterId{}", theaterId);
            throw new MovieBookingException("Failed to update seat status");

        }

    }
}
