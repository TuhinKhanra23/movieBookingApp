package com.cts.fse.service;

import com.cts.fse.model.Theater;
import com.cts.fse.repository.TheaterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MovieService implements IMovieService {

    @Autowired
    private TheaterRepo theaterRepo;

    @Override
    public ResponseEntity<String> addTheater(Theater theater) {
        long totalTheater = theaterRepo.count();
        if (!theaterRepo.findByTheaterNameAndTheaterLoc(theater.getTheaterName(), theater.getTheaterLoc()).isEmpty()) {
            return new ResponseEntity<>("Theater already present with this name and location!", HttpStatus.CONFLICT);
        } else {
            if (totalTheater == 0) {
                theater.setTheaterId(1);
            } else {
                theater.setTheaterId((int) totalTheater + 1);
            }
            theaterRepo.save(theater);
            return new ResponseEntity<>("Theater added Successfully!", HttpStatus.OK);

        }

    }
}
