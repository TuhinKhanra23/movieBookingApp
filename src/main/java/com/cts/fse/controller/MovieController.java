package com.cts.fse.controller;

import com.cts.fse.dto.BookTicketReqDTO;
import com.cts.fse.dto.MovieSearchResDTO;
import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.model.Movie;
import com.cts.fse.model.Theater;
import com.cts.fse.service.IMovieService;
import com.cts.fse.utils.JwtUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class MovieController {
    @Autowired
    private IMovieService movieService;
    @Autowired
    JwtUtil jwtUtil;

    @ApiOperation(value = "Add Theater", response = ResponseEntity.class)
    @PostMapping("/addTheater")
    public ResponseEntity<String> addTheater(@RequestHeader String token, @RequestBody Theater theater) throws MovieBookingException {
        log.info("Inside /addTheater Api Controller for TheaterName{}", theater.getTheaterName());
        if (jwtUtil.validateToken(token)) {
            log.info("JWT Token validated");
            return movieService.addTheater(theater);
        } else {
            throw new MovieBookingException("Invalid Token");
        }
    }

    @ApiOperation(value = "Add Movie", response = ResponseEntity.class)
    @PostMapping("/addMovie")
    public ResponseEntity<String> addMovie(@RequestHeader String token, @RequestBody Movie movie) throws MovieBookingException {
        log.info("Inside /addMovie Api Controller with movieName{}", movie.getMovieName());
        if (jwtUtil.validateToken(token)) {
            log.info("JWT Token validated ");
            return movieService.addMovie(movie);
        } else {
            throw new MovieBookingException("Invalid Token");
        }
    }

    @ApiOperation(value = "Show all movies", response = ResponseEntity.class)
    @GetMapping("/all")
    public List<MovieSearchResDTO> searchAllMovies() {
        log.info("Inside /all Api Controller ");
        return movieService.searchAllMovies();
    }

    @ApiOperation(value = "Search Movies", response = ResponseEntity.class)
    @GetMapping("/movies/search")
    public List<MovieSearchResDTO> searchMovie(@RequestHeader String token, @RequestParam String movieName) throws MovieBookingException {
        log.info("Inside /movies/search Api Controller with movieName{}", movieName);
        if (jwtUtil.validateToken(token)) {
            log.info("Jwt Token validated ");
            return movieService.searchMovie(movieName);
        } else {
            throw new MovieBookingException("Invalid Token");
        }
    }

    @ApiOperation(value = "Delete Movie", response = ResponseEntity.class)
    @PostMapping("/delete")
    public ResponseEntity<String> deleteMovie(@RequestHeader String token, @RequestParam(required = true) String movieId) throws MovieBookingException {
        log.info("Inside /delete Api Controller with movieId{}", movieId);
        if (jwtUtil.validateToken(token)) {
            log.info(" JWT Token validated ");
            return movieService.deleteMovie(movieId);
        } else {
            throw new MovieBookingException("Invalid Token");
        }
    }

    @ApiOperation(value = "Book Tickets", response = ResponseEntity.class)
    @PostMapping("/add")
    public ResponseEntity<String> bookTicket(@RequestHeader String token, @RequestBody BookTicketReqDTO bookTicketReqDTO) throws MovieBookingException {
        log.info("Inside /add Api Controller");
        if (jwtUtil.validateToken(token)) {
            String userId = jwtUtil.extractUsername(token);
            log.info("JWT Token validated for UserId{}", userId);
            return movieService.bookTicket(bookTicketReqDTO, userId);
        } else {
            throw new MovieBookingException("Invalid Token");
        }
    }

}
