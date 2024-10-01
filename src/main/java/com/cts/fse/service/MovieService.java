package com.cts.fse.service;

import com.cts.fse.config.MongoConnectionConfig;
import com.cts.fse.dto.AddMovieReqDto;
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

import java.text.SimpleDateFormat;
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
                theater.setTheaterId((int) totalTheater + 30);
            }
            theaterRepo.save(theater);
            log.info("Theater added Successfully!");
            return new ResponseEntity<>("Theater added Successfully!", HttpStatus.OK);

        }

    }

    @Override
    public ResponseEntity<String> addMovie(AddMovieReqDto movieInReq) {
        log.info("Inside addMovie Service Method :");
        Movie movie = new Movie();
        long totalMovie = movieRepo.count();
        if (totalMovie == 0) {
            movie.setMovieId("20");
        } else {
            movie.setMovieId(String.valueOf((int) totalMovie + 1));
        }
        movie.setMovieName(movieInReq.getMovieName());
        movie.setReleaseDate(movieInReq.getReleaseDate());
        List<Integer> theaterList = new ArrayList<>();
        String[] theaterArray = movieInReq.getTheaterIdList().split(",");
        for (String ids : theaterArray) {
            theaterList.add(Integer.parseInt(ids));
        }
        movie.setTheaterIdList(theaterList);

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

                    MovieSearchResDTO movieSearchResDTO = new MovieSearchResDTO();
                    movieSearchResDTO.setMovieId(movie.getMovieId());
                    movieSearchResDTO.setMovieName(movie.getMovieName());
                    movieSearchResDTO.setReleaseDate(movie.getReleaseDate());



                    log.info(" theater added to res list");
                    movieSearchResDTOList.add(movieSearchResDTO);
                }


        log.info("final movie res list size {}", movieSearchResDTOList.size());
        return movieSearchResDTOList;

    }

    @Override
    public ResponseEntity<String> deleteMovie(String movieId) throws MovieBookingException {
        log.info("Inside deleteMovie Service Method :");
        Optional<Movie> movieInLedger = movieRepo.findById(movieId);
        if (movieInLedger.isPresent()) {
            for (Integer theaterId : movieInLedger.get().getTheaterIdList()) {
                log.info("Inside deleteMovie Service theater Id :{}", theaterId);
                Optional<Theater> theaterInLedger = theaterRepo.findById(theaterId);
                Set<String> stringSet = new HashSet<>();
                if (theaterInLedger.isPresent()) {
                    log.info("theater present theater Id :{}", theaterId);
                    log.info("theater bookedTickets :{}", theaterInLedger.get().getBookedTickets());
                    theaterInLedger.get().setBookedTickets(stringSet);
                    log.info("theater bookedTickets after:{}", theaterInLedger.get().getBookedTickets());
                    theaterRepo.save(theaterInLedger.get());
                }

            }
            List<Ticket> ticketInLedger = ticketRepo.findByMovieName(movieInLedger.get().getMovieName());
            if (!ticketInLedger.isEmpty()) {
                for (Ticket ticket : ticketInLedger) {
                    ticketRepo.deleteById(ticket.getTicketId());
                }
            }
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
        if (null != bookTicketReqDTO.getMovieName()) {
            List<Movie> movieInLedger = movieRepo.findByMovieName(bookTicketReqDTO.getMovieName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            if (!movieInLedger.isEmpty()) {

                if (movieInLedger.get(0).getReleaseDate().after(date)) {
                    log.info("Movie is yet to be released!");
                    throw new MovieBookingException("Movie is yet to be released!");


                }
            }
        } else {
            throw new MovieBookingException("Movie name cannot be null!");
        }
        if (null != bookTicketReqDTO.getTheaterName()) {
            List<Theater> theaterInLedger = theaterRepo.findByTheaterName(bookTicketReqDTO.getTheaterName());
            if (!theaterInLedger.isEmpty()) {
                Set<String> bookedTickets = new HashSet<>();
                if (null != theaterInLedger.get(0).getBookedTickets() && !theaterInLedger.get(0).getBookedTickets().isEmpty()) {
                    bookedTickets = theaterInLedger.get(0).getBookedTickets();

                }

                bookedTickets.addAll(bookTicketReqDTO.getSeatNumber());
                theaterInLedger.get(0).setBookedTickets(bookedTickets);
                log.info("Seat booked for theater");

                theaterRepo.save(theaterInLedger.get(0));
            }
        } else {
            throw new MovieBookingException("Theater name cannot be null!");
        }

        Ticket ticket = new Ticket();
        ticket.setBookingDate(new Date());
        ticket.setMovieName(bookTicketReqDTO.getMovieName());
        ticket.setUserId(userId);
        ticket.setTheaterName(bookTicketReqDTO.getTheaterName());
        ticket.setSeatNumber(bookTicketReqDTO.getSeatNumber());
        long totalTicket = ticketRepo.count();
        if (totalTicket == 0) {
            ticket.setTicketId("1");

        } else {
            ticket.setTicketId(String.valueOf((int) totalTicket + 20));
        }
        ticketRepo.save(ticket);
        log.info("Ticket Booked Successfully.");
        return new ResponseEntity<>("Ticket Booked Successfully..", HttpStatus.OK);
    }

    @Override
    public List<MovieSearchResDTO> searchAllMoviesForList() {
        log.info("Inside searchAllMoviesForList Service Method :");
        List<MovieSearchResDTO> movieSearchResDTOList = new ArrayList<>();
        List<Movie> movieList = movieRepo.findAll();
        log.info("total movie  in repo :{}", movieList.size());

        for (Movie movie : movieList) {

            log.info("inside movie  id :{}", movie.getMovieId());

            log.info("total theater  number for this movie is :{}", movie.getTheaterIdList().size());

            MovieSearchResDTO movieSearchResDTO = new MovieSearchResDTO();
            movieSearchResDTO.setMovieId(movie.getMovieId());
            movieSearchResDTO.setMovieName(movie.getMovieName());
            movieSearchResDTO.setReleaseDate(movie.getReleaseDate());


            movieSearchResDTOList.add(movieSearchResDTO);
        }

        log.info("final response list size  :{}", movieSearchResDTOList.size());
        return movieSearchResDTOList;
    }

    @Override
    public List<MovieSearchResDTO> searchMovieByName(String movieName) {
        {
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

                for (Integer tid : movie.getTheaterIdList()) {
                    log.info("inside theater id :{}", tid);

                    Optional<Theater> theater = theaterRepo.findById(tid);
                    if (theater.isPresent()) {
                        MovieSearchResDTO movieSearchResDTO = new MovieSearchResDTO();
                        movieSearchResDTO.setMovieId(movie.getMovieId());
                        movieSearchResDTO.setMovieName(movie.getMovieName());
                        movieSearchResDTO.setReleaseDate(movie.getReleaseDate());
                        movieSearchResDTO.setTheaterId(theater.get().getTheaterId());
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

            log.info("final movie res list size {}", movieSearchResDTOList.size());
            return movieSearchResDTOList;

        }
    }

    @Override
    public Set<String> fetchBookedTickets(String theaterName) {
        log.info("Inside fetchBookedTickets Service Method :");
        List<Theater> theaterList = theaterRepo.findByTheaterName(theaterName);
        Theater theater = theaterList.get(0);
        return theater.getBookedTickets();
    }
}
