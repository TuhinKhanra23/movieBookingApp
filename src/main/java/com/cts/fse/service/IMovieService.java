package com.cts.fse.service;

import com.cts.fse.dto.AddMovieReqDto;
import com.cts.fse.dto.BookTicketReqDTO;
import com.cts.fse.dto.MovieSearchResDTO;
import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.model.Theater;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface IMovieService {
    ResponseEntity<String> addTheater(Theater theater) throws MovieBookingException;

    ResponseEntity<String> addMovie(AddMovieReqDto movie);

    List<MovieSearchResDTO> searchAllMovies();

    List<MovieSearchResDTO> searchMovie(String movieName);

    ResponseEntity<String> deleteMovie(String movieId) throws MovieBookingException;

    ResponseEntity<String> bookTicket(BookTicketReqDTO bookTicketReqDTO, String userId) throws MovieBookingException;

    List<MovieSearchResDTO> searchAllMoviesForList();

    List<MovieSearchResDTO> searchMovieByName(String movieName);

    Set<String> fetchBookedTickets(String theaterName);
}
