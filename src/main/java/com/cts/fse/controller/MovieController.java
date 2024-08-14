package com.cts.fse.controller;

import com.cts.fse.model.Theater;
import com.cts.fse.model.User;
import com.cts.fse.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {
    @Autowired
    private IMovieService movieService;

    @PostMapping("/addTheater")
    public ResponseEntity<String> addTheater(@RequestBody Theater theater) {
        return movieService.addTheater(theater);
    }

}
