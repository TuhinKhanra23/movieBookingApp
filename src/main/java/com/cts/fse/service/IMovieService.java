package com.cts.fse.service;

import com.cts.fse.dto.BookTicketReqDTO;
import com.cts.fse.dto.MovieSearchResDTO;
import com.cts.fse.model.Movie;
import com.cts.fse.model.Theater;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IMovieService {
    ResponseEntity<String> addTheater(Theater theater);

    ResponseEntity<String> addMovie(Movie movie);

    List<MovieSearchResDTO> searchAllMovies();

    List<MovieSearchResDTO> searchMovie(String movieName);

    ResponseEntity<String> deleteMovie(String movieId);

    ResponseEntity<String> bookTicket(BookTicketReqDTO bookTicketReqDTO);
}
