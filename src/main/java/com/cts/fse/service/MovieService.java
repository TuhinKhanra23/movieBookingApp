package com.cts.fse.service;

import com.cts.fse.config.MongoConnectionConfig;
import com.cts.fse.dto.BookTicketReqDTO;
import com.cts.fse.dto.MovieSearchResDTO;
import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.model.Movie;
import com.cts.fse.model.Theater;
import com.cts.fse.model.Ticket;
import com.cts.fse.repository.MovieRepo;
import com.cts.fse.repository.TheaterRepo;
import com.cts.fse.repository.TicketRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
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
    public ResponseEntity<String> addTheater(Theater theater) throws MovieBookingException {
        log.info("Inside addTheater Service Method :");
        long totalTheater = theaterRepo.count();

        if (!theaterRepo.findByTheaterNameAndTheaterLoc(theater.getTheaterName(), theater.getTheaterLoc()).isEmpty()) {
            log.info("Theater already present with this name and location!");
            throw new MovieBookingException("Theater already present with this name and location!");
        } else {

            if (totalTheater == 0) {
                theater.setTheaterId(1);
            } else {
                theater.setTheaterId((int) totalTheater + 1);
            }
            theaterRepo.save(theater);
            log.info("Theater added Successfully!");
            return new ResponseEntity<>("Theater added Successfully!", HttpStatus.OK);

        }

    }

    @Override
    public ResponseEntity<String> addMovie(Movie movie) {
        log.info("Inside addMovie Service Method :");
        long totalMovie = movieRepo.count();
        if (totalMovie == 0) {
            movie.setMovieId("1");
        } else {
            movie.setMovieId(String.valueOf((int) totalMovie + 1));
        }
        movieRepo.save(movie);
        log.info("Movie added Successfully!");
        return new ResponseEntity<>("Movie added Successfully!", HttpStatus.OK);

    }

    @Override
    public List<MovieSearchResDTO> searchAllMovies() {
        log.info("Inside searchAllMovies Service Method :");
        List<MovieSearchResDTO> movieSearchResDTOList = new ArrayList<>();
        List<Movie> movieList = movieRepo.findAll();
        log.info("total movie in repo :{}", movieList.size());

        for (Movie movie : movieList) {

            log.info("inside movie id :{}", movie.getMovieId());

            log.info("total theater number for this movie is :{}", movie.getTheaterIdList().size());
            for (Integer tid : movie.getTheaterIdList()) {
                log.info("inside theater id :{}", tid);

                Optional<Theater> theater = theaterRepo.findById(tid);
                if (theater.isPresent()) {
                    MovieSearchResDTO movieSearchResDTO = new MovieSearchResDTO();
                    movieSearchResDTO.setMovieId(movie.getMovieId());
                    movieSearchResDTO.setMovieName(movie.getMovieName());
                    movieSearchResDTO.setReleaseDate(movie.getReleaseDate());
                    movieSearchResDTO.setTheaterName(theater.get().getTheaterName());
                    movieSearchResDTO.setTheaterLoc(theater.get().getTheaterLoc());
                    movieSearchResDTO.setTheaterCapacity(theater.get().getTheaterCapacity());

                    if (null != theater.get().getBookedTickets() && !theater.get().getBookedTickets().isEmpty()) {
                        movieSearchResDTO.setBookedTickets(theater.get().getBookedTickets());
                    }
                    log.info("theater added to response list with id :{}", tid);
                    movieSearchResDTOList.add(movieSearchResDTO);
                }
            }
        }
        log.info("final response list size :{}", movieSearchResDTOList.size());
        return movieSearchResDTOList;
    }

    @Override
    public List<MovieSearchResDTO> searchMovie(String movieName) {
        log.info("Inside searchMovie Service Method :");
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
        log.info("total  movie returned :{}", movieList.size());

        for (Movie movie : movieList) {
            log.info("inside movie id:{}", movie.getMovieId());
            log.info("total theater number for this movie is:{}", movie.getTheaterIdList().size());
            for (Integer tid : movie.getTheaterIdList()) {

                log.info("inside theater loop for theater id:{}", tid);
                Optional<Theater> theater = theaterRepo.findById(tid);
                if (theater.isPresent()) {
                    MovieSearchResDTO movieSearchResDTO = new MovieSearchResDTO();
                    movieSearchResDTO.setMovieId(movie.getMovieId());
                    movieSearchResDTO.setMovieName(movie.getMovieName());
                    movieSearchResDTO.setReleaseDate(movie.getReleaseDate());
                    movieSearchResDTO.setTheaterName(theater.get().getTheaterName());
                    movieSearchResDTO.setTheaterLoc(theater.get().getTheaterLoc());
                    movieSearchResDTO.setTheaterCapacity(theater.get().getTheaterCapacity());

                    if (null != theater.get().getBookedTickets() && !theater.get().getBookedTickets().isEmpty()) {
                        movieSearchResDTO.setBookedTickets(theater.get().getBookedTickets());
                    }
                    log.info(" theater added to res list");
                    movieSearchResDTOList.add(movieSearchResDTO);
                }
            }
        }
        log.info("final movie res list size {}", movieSearchResDTOList.size());
        return movieSearchResDTOList;

    }

    @Override
    public ResponseEntity<String> deleteMovie(String movieId) throws MovieBookingException {
        log.info("Inside deleteMovie Service Method :");
        Optional<Movie> movieInLedger = movieRepo.findById(movieId);
        if (movieInLedger.isPresent()) {
            movieRepo.deleteById(movieId);
            log.info("Movie Deleted Successfully for movieId :{}", movieId);
            return new ResponseEntity<>("Movie Deleted Successfully", HttpStatus.OK);
        } else {
            log.info("Movie not found for movieId :{}", movieId);
            throw new MovieBookingException("Movie not found for movieId " + movieId);


        }
    }

    @Override
    public ResponseEntity<String> bookTicket(BookTicketReqDTO bookTicketReqDTO, String userId) throws MovieBookingException {
        log.info("Inside bookTicket Service Method :");
        if (null != bookTicketReqDTO.getMovieId()) {
            Optional<Movie> movieInLedger = movieRepo.findById(bookTicketReqDTO.getMovieId());
            if (movieInLedger.isPresent()) {

                if (movieInLedger.get().getReleaseDate().after(bookTicketReqDTO.getBookingDate())) {
                    log.info("Movie is yet to be released!");
                    throw new MovieBookingException("Movie is yet to be released!");


                } else if (!movieInLedger.get().getTheaterIdList().contains(bookTicketReqDTO.getTheaterId())) {
                    log.info("Movie is not released in this theater!");
                    throw new MovieBookingException("Movie is not released in this theater!!");


                }
            }
        }
        if (null != bookTicketReqDTO.getTheaterId()) {
            Optional<Theater> theaterInLedger = theaterRepo.findById(bookTicketReqDTO.getTheaterId());
            if (theaterInLedger.isPresent()) {
                Set<Integer> bookedTickets = new HashSet<>();
                if (null != theaterInLedger.get().getBookedTickets() && !theaterInLedger.get().getBookedTickets().isEmpty()) {
                    bookedTickets = theaterInLedger.get().getBookedTickets();
                    Set<Integer> commonSeats = new HashSet<>(bookedTickets);
                    commonSeats.retainAll(bookTicketReqDTO.getSeatNumber());
                    if (!commonSeats.isEmpty()) {
                        log.info("Seat is already booked");
                        throw new MovieBookingException("Seat is already booked ");


                    }
                }
                for (Integer seat : bookTicketReqDTO.getSeatNumber()) {
                    if (seat > theaterInLedger.get().getTheaterCapacity()) {
                        log.info("Seat number is invalid");
                        throw new MovieBookingException("Seat number is invalid " + seat);

                    }
                }
                bookedTickets.addAll(bookTicketReqDTO.getSeatNumber());
                theaterInLedger.get().setBookedTickets(bookedTickets);
                log.info("Seat booked for theater");

                theaterRepo.save(theaterInLedger.get());
            }
        }

        Ticket ticket = new Ticket();
        ticket.setBookingDate(bookTicketReqDTO.getBookingDate());
        ticket.setMovieId(bookTicketReqDTO.getMovieId());
        ticket.setUserId(userId);
        ticket.setTheaterId(bookTicketReqDTO.getTheaterId());
        ticket.setSeatNumber(bookTicketReqDTO.getSeatNumber());
        long totalTicket = ticketRepo.count();
        if (totalTicket == 0) {
            ticket.setTicketId("1");

        } else {
            ticket.setTicketId(String.valueOf((int) totalTicket + 1));
        }
        ticketRepo.save(ticket);
        log.info("Ticket Booked Successfully.");
        return new ResponseEntity<>("Ticket Booked Successfully..", HttpStatus.OK);
    }
}
