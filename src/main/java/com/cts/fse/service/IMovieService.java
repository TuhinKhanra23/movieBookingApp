package com.cts.fse.service;

import com.cts.fse.model.Theater;
import org.springframework.http.ResponseEntity;

public interface IMovieService {
    ResponseEntity<String> addTheater(Theater theater);
}
