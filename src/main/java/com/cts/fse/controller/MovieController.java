package com.cts.fse.controller;

import com.cts.fse.dto.AddMovieReqDto;
import com.cts.fse.dto.BookTicketReqDTO;
import com.cts.fse.dto.MovieSearchResDTO;
import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.model.Theater;
import com.cts.fse.service.IMovieService;
import com.cts.fse.utils.JwtUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
@Slf4j
@RestController
@CrossOrigin
public class MovieController {
    @Autowired
    private IMovieService movieService;
    @Autowired
    JwtUtil jwtUtil;


    @ApiOperation(value = "Add Theater", response = ResponseEntity.class)
    @PostMapping("/addTheater")
    public ResponseEntity<String> addTheater(@RequestParam(required = true) String token, @RequestBody Theater theater) throws MovieBookingException {
        log.info("Inside /addTheater Api Controller for TheaterName{}", theater.getTheaterName());
        log.info(token);
        if (jwtUtil.validateToken(token)) {
            log.info("JWT Token validated");
            return movieService.addTheater(theater);
        } else {
            throw new MovieBookingException("Invalid Token");
        }
    }

    @ApiOperation(value = "Add Movie", response = ResponseEntity.class)
    @PostMapping("/addMovie")
    public ResponseEntity<String> addMovie(@RequestParam(required = true) String token, @RequestBody AddMovieReqDto movie) throws MovieBookingException {
        log.info("Inside /addMovie Api Controller with movieName{}", movie.getMovieName());
        log.info(String.valueOf(movie));
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
        return movieService.searchAllMoviesForList();
    }

    @ApiOperation(value = "Show all movies", response = ResponseEntity.class)
    @GetMapping("/allMovies")
    public List<MovieSearchResDTO> searchAllMoviesForBooking() {
        log.info("Inside /allMovies Api Controller ");
        return movieService.searchAllMovies();
    }

    @ApiOperation(value = "Search Movies", response = ResponseEntity.class)
    @GetMapping("/movies/search")
    public List<MovieSearchResDTO> searchMovie(@RequestParam String movieName) throws MovieBookingException {
        log.info("Inside /movies/search Api Controller with movieName{}", movieName);

            return movieService.searchMovie(movieName);

    }

    @ApiOperation(value = "Delete Movie", response = ResponseEntity.class)
    @PostMapping("/delete")
    public ResponseEntity<String> deleteMovie(@RequestParam(required = true) String token, @RequestParam(required = true) String movieId) throws MovieBookingException {
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
    public ResponseEntity<String> bookTicket(@RequestParam(required = true) String token, @RequestBody BookTicketReqDTO bookTicketReqDTO) throws MovieBookingException {
        log.info("Inside /add Api Controller{}", bookTicketReqDTO);
        if (jwtUtil.validateToken(token)) {
            String userId = jwtUtil.extractUsername(token);
            log.info("JWT Token validated for UserId{}", userId);
            return movieService.bookTicket(bookTicketReqDTO, userId);
        } else {
            throw new MovieBookingException("Invalid Token");
        }
    }

    @ApiOperation(value = "Search Theater By Movie", response = ResponseEntity.class)
    @GetMapping("/getTheater")
    public List<MovieSearchResDTO> searchMovieByName(@RequestParam String movieName) throws MovieBookingException {
        log.info("Inside /movies/search Api Controller with movieName{}", movieName);

        return movieService.searchMovieByName(movieName);

    }

    @ApiOperation(value = "Search Theater By Movie", response = ResponseEntity.class)
    @GetMapping("/fetchBookedTickets")
    public Set<String> fetchBookedTickets(@RequestParam String theaterName) throws MovieBookingException {
        log.info("Inside /movies/search Api Controller with theaterName{}", theaterName);

        return movieService.fetchBookedTickets(theaterName);

    }

}
