package com.cts.fse.controller;

import com.cts.fse.dto.BookTicketReqDTO;
import com.cts.fse.dto.MovieSearchResDTO;
import com.cts.fse.model.Movie;
import com.cts.fse.model.Theater;
import com.cts.fse.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {
    @Autowired
    private IMovieService movieService;

    @PostMapping("/addTheater")
    public ResponseEntity<String> addTheater(@RequestBody Theater theater) {
        return movieService.addTheater(theater);
    }

    @PostMapping("/addMovie")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    @GetMapping("/all")
    public List<MovieSearchResDTO> searchAllMovies() {
        return movieService.searchAllMovies();
    }

    @GetMapping("/movies/search")
    public List<MovieSearchResDTO> searchMovie(@RequestParam String movieName) {
        return movieService.searchMovie(movieName);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteMovie(@RequestParam(required = true) String movieId) {
        return movieService.deleteMovie(movieId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> bookTicket(@RequestBody BookTicketReqDTO bookTicketReqDTO) {
        return movieService.bookTicket(bookTicketReqDTO);
    }

}
