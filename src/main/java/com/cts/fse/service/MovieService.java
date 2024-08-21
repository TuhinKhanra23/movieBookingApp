package com.cts.fse.service;

import com.cts.fse.config.MongoConnectionConfig;
import com.cts.fse.dto.BookTicketReqDTO;
import com.cts.fse.dto.MovieSearchResDTO;
import com.cts.fse.model.Movie;
import com.cts.fse.model.Theater;
import com.cts.fse.model.Ticket;
import com.cts.fse.repository.MovieRepo;
import com.cts.fse.repository.TheaterRepo;
import com.cts.fse.repository.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieService implements IMovieService {

    @Autowired
    MongoConnectionConfig mongoConnectionConfig;
    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    TicketRepo ticketRepo;
    @Autowired
    private TheaterRepo theaterRepo;
    @Autowired
    private MovieRepo movieRepo;

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

    @Override
    public ResponseEntity<String> addMovie(Movie movie) {
        long totalMovie = movieRepo.count();
        if (totalMovie == 0) {
            movie.setMovieId("1");
        } else {
            movie.setMovieId(String.valueOf((int) totalMovie + 1));
        }
        movieRepo.save(movie);
        return new ResponseEntity<>("Movie added Successfully!", HttpStatus.OK);

    }

    @Override
    public List<MovieSearchResDTO> searchAllMovies() {
        List<MovieSearchResDTO> movieSearchResDTOList = new ArrayList<>();
        List<Movie> movieList = movieRepo.findAll();
        System.out.println("total movie in repo" + movieList.size());
        for (Movie movie : movieList) {
            System.out.println("inside movie id" + movie.getMovieId());
            System.out.println("total theater number for this movie is" + movie.getTheaterIdList().size());
            for (Integer tid : movie.getTheaterIdList()) {
                System.out.println("inside theater id" + tid);
                Optional<Theater> theater = theaterRepo.findById(tid);
                if (theater.isPresent()) {
                    MovieSearchResDTO movieSearchResDTO = new MovieSearchResDTO();
                    movieSearchResDTO.setMovieId(movie.getMovieId());
                    movieSearchResDTO.setMovieName(movie.getMovieName());
                    movieSearchResDTO.setReleaseDate(movie.getReleaseDate());
                    movieSearchResDTO.setTheaterName(theater.get().getTheaterName());
                    movieSearchResDTO.setTheaterLoc(theater.get().getTheaterLoc());
                    movieSearchResDTO.setTheaterCapacity(theater.get().getTheaterCapacity());

                    if (null != theater.get().getBookedTickets() || !theater.get().getBookedTickets().isEmpty()) {
                        movieSearchResDTO.setBookedTickets(theater.get().getBookedTickets());
                    }
                    movieSearchResDTOList.add(movieSearchResDTO);
                }
            }
        }
        return movieSearchResDTOList;
    }

    @Override
    public List<MovieSearchResDTO> searchMovie(String movieName) {

        List<MovieSearchResDTO> movieSearchResDTOList = new ArrayList<>();

        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();


        if (null != movieName && !movieName.isEmpty()) {
            criteria.add(Criteria.where("movieName").regex(movieName, "i"));
        }
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }
        List<Movie> movieList = mongoOperations.find(query, Movie.class);
        System.out.println("total  movie returned" + movieList.size());
        for (Movie movie : movieList) {
            System.out.println("inside movie id" + movie.getMovieId());
            System.out.println("total theater number for this movie is" + movie.getTheaterIdList().size());
            for (Integer tid : movie.getTheaterIdList()) {
                System.out.println("inside theater id" + tid);
                Optional<Theater> theater = theaterRepo.findById(tid);
                if (theater.isPresent()) {
                    MovieSearchResDTO movieSearchResDTO = new MovieSearchResDTO();
                    movieSearchResDTO.setMovieId(movie.getMovieId());
                    movieSearchResDTO.setMovieName(movie.getMovieName());
                    movieSearchResDTO.setReleaseDate(movie.getReleaseDate());
                    movieSearchResDTO.setTheaterName(theater.get().getTheaterName());
                    movieSearchResDTO.setTheaterLoc(theater.get().getTheaterLoc());
                    movieSearchResDTO.setTheaterCapacity(theater.get().getTheaterCapacity());

                    if (null != theater.get().getBookedTickets() || !theater.get().getBookedTickets().isEmpty()) {
                        movieSearchResDTO.setBookedTickets(theater.get().getBookedTickets());
                    }
                    movieSearchResDTOList.add(movieSearchResDTO);
                }
            }
        }
        return movieSearchResDTOList;

    }

    @Override
    public ResponseEntity<String> deleteMovie(String movieId) {
        Optional<Movie> movieInLedger = movieRepo.findById(movieId);
        if (movieInLedger.isPresent()) {
            movieRepo.deleteById(movieId);
            return new ResponseEntity<>("Movie Deleted Successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> bookTicket(BookTicketReqDTO bookTicketReqDTO) {

        if (null != bookTicketReqDTO.getMovieId()) {
            Optional<Movie> movieInLedger = movieRepo.findById(bookTicketReqDTO.getMovieId());
            if (movieInLedger.isPresent()) {

                if (movieInLedger.get().getReleaseDate().after(bookTicketReqDTO.getBookingDate())) {
                    return new ResponseEntity<>("Movie is yet to be released!", HttpStatus.BAD_REQUEST);
                } else if (!movieInLedger.get().getTheaterIdList().contains(bookTicketReqDTO.getTheaterId())) {
                    return new ResponseEntity<>("Movie is not released in this theater!!", HttpStatus.BAD_REQUEST);
                }
            }
        }
        if (null != bookTicketReqDTO.getTheaterId()) {
            Optional<Theater> theaterInLedger = theaterRepo.findById(bookTicketReqDTO.getTheaterId());
            if (theaterInLedger.isPresent()) {
                if (null != theaterInLedger.get().getBookedTickets() || theaterInLedger.get().getBookedTickets().isEmpty()) {
                    Set<Integer> bookedTickets = theaterInLedger.get().getBookedTickets();
                    Set<Integer> commonSeats = new HashSet<>(bookedTickets);
                    commonSeats.retainAll(bookTicketReqDTO.getSeatNumber());
                    if (!commonSeats.isEmpty()) {
                        return new ResponseEntity<>("Seat is already booked", HttpStatus.BAD_REQUEST);

                    } else {
                        for (Integer seat : bookTicketReqDTO.getSeatNumber()) {
                            if (seat > theaterInLedger.get().getTheaterCapacity()) {
                                return new ResponseEntity<>("Seat number is invalid", HttpStatus.BAD_REQUEST);
                            }
                        }
                        bookedTickets.addAll(bookTicketReqDTO.getSeatNumber());
                        theaterInLedger.get().setBookedTickets(bookedTickets);
                    }
                }
                theaterRepo.save(theaterInLedger.get());
            }
        }

        Ticket ticket = new Ticket();
        ticket.setBookingDate(bookTicketReqDTO.getBookingDate());
        ticket.setMovieId(bookTicketReqDTO.getMovieId());
        ticket.setUserId(bookTicketReqDTO.getLoginId());
        ticket.setTheaterId(bookTicketReqDTO.getTheaterId());
        ticket.setSeatNumber(bookTicketReqDTO.getSeatNumber());
        long totalTicket = ticketRepo.count();
        if (totalTicket == 0) {
            ticket.setTicketId("1");

        } else {
            ticket.setTicketId(String.valueOf((int) totalTicket + 1));
        }
        ticketRepo.save(ticket);
        return new ResponseEntity<>("Ticket Booked Successfully..", HttpStatus.OK);
    }
}
