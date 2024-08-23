package com.cts.fse.service;

import com.cts.fse.dto.BookTicketReqDTO;
import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.model.Movie;
import com.cts.fse.model.Theater;
import com.cts.fse.repository.MovieRepo;
import com.cts.fse.repository.TheaterRepo;
import com.cts.fse.repository.TicketRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @InjectMocks
    MovieService movieService;

    @Mock
    private TheaterRepo theaterRepo;
    @Mock
    private MovieRepo movieRepo;
    @Mock
    MongoOperations mongoOperations;
    @Mock
    TicketRepo ticketRepo;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void addTheater() throws MovieBookingException {
        Theater theater=new Theater();
        theater.setTheaterName("t1");
        theater.setTheaterLoc("tloc1");

        List<Theater> demoTheaterList = new ArrayList<>();

        Mockito.when(theaterRepo.findByTheaterNameAndTheaterLoc("t1","tloc1")).thenReturn(demoTheaterList);
        Mockito.when(theaterRepo.count()).thenReturn((long)0);
        Assertions.assertEquals(HttpStatus.OK, movieService.addTheater(theater).getStatusCode());
    }

    @Test
    void addTheater1() throws MovieBookingException {
        Theater theater=new Theater();
        theater.setTheaterName("t1");
        theater.setTheaterLoc("tloc1");

        List<Theater> demoTheaterList = new ArrayList<>();

        Mockito.when(theaterRepo.findByTheaterNameAndTheaterLoc("t1","tloc1")).thenReturn(demoTheaterList);
        Mockito.when(theaterRepo.count()).thenReturn((long)1);
        Assertions.assertEquals(HttpStatus.OK, movieService.addTheater(theater).getStatusCode());
    }

    @Test
    void addTheater2() throws MovieBookingException {
        Theater theater=new Theater();
        theater.setTheaterName("t1");
        theater.setTheaterLoc("tloc1");

        List<Theater> demoTheaterList = new ArrayList<>();
        demoTheaterList.add(theater);

        Mockito.when(theaterRepo.findByTheaterNameAndTheaterLoc("t1","tloc1")).thenReturn(demoTheaterList);

        Assertions.assertEquals(HttpStatus.CONFLICT, movieService.addTheater(theater).getStatusCode());
    }

    @Test
    void addMovie() {
        Movie movie = new Movie();
        Mockito.when(movieRepo.count()).thenReturn((long)0);
        movieService.addMovie(movie);
        Assertions.assertEquals(HttpStatus.OK, movieService.addMovie(movie).getStatusCode());
    }

    @Test
    void addMovie1() {
        Movie movie = new Movie();
        Mockito.when(movieRepo.count()).thenReturn((long)1);
        movieService.addMovie(movie);
        Assertions.assertEquals(HttpStatus.OK, movieService.addMovie(movie).getStatusCode());
    }

    @Test
    void searchAllMovies() {
        List<Movie> demoMovieList = new ArrayList<>();
        Movie movie=new Movie();
        movie.setMovieId("123");
        movie.setMovieName("abc");
        movie.setReleaseDate(new Date());
        movie.setTheaterIdList(Arrays.asList(1));

        demoMovieList.add(movie);

        Mockito.when(movieRepo.findAll()).thenReturn(demoMovieList);

        Theater theater=new Theater();
        theater.setTheaterName("t1");
        theater.setTheaterLoc("tloc1");
        theater.setTheaterId(1);
        theater.setTheaterCapacity(100);
        theater.setBookedTickets(new HashSet<>(Arrays.asList(20,21)));

        Mockito.when(theaterRepo.findById(1)).thenReturn(Optional.of(theater));

        Assertions.assertEquals(1,movieService.searchAllMovies().size());

    }

    @Test
    void searchMovie() {
        List<Movie> demoMovieList = new ArrayList<Movie>();
        Movie movie=new Movie();
        movie.setMovieId("123");
        movie.setMovieName("abc");
        movie.setReleaseDate(new Date());
        movie.setTheaterIdList(Arrays.asList(1));
        demoMovieList.add(movie);
        Mockito.when(mongoOperations.find(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(movie));

        Theater theater=new Theater();
        theater.setTheaterName("t1");
        theater.setTheaterLoc("tloc1");
        theater.setTheaterId(1);
        theater.setTheaterCapacity(100);
        theater.setBookedTickets(new HashSet<>(Arrays.asList(20,21)));
        Mockito.when(theaterRepo.findById(1)).thenReturn(Optional.of(theater));
        Assertions.assertEquals(1,movieService.searchMovie("abc").size());
    }

    @Test
    void deleteMovie() throws MovieBookingException {
        Movie movie=new Movie();
        movie.setMovieId("123");
        movie.setMovieName("abc");
        movie.setReleaseDate(new Date());
        movie.setTheaterIdList(Arrays.asList(1));
        Mockito.when(movieRepo.findById("123")).thenReturn(Optional.of(movie));
        Mockito.doNothing().when(movieRepo).deleteById("123");

        Assertions.assertEquals(HttpStatus.OK, movieService.deleteMovie("123").getStatusCode());
    }



    @Test
    void bookTicket() throws ParseException, MovieBookingException {


        DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        Date releaseDate = format.parse("20240821");
        Date bookingDate = format.parse("20240822");
        BookTicketReqDTO demoBookTicketReqDTO = new BookTicketReqDTO();
        demoBookTicketReqDTO.setBookingDate(bookingDate);
        demoBookTicketReqDTO.setMovieId("123");
        demoBookTicketReqDTO.setTheaterId(1);

        demoBookTicketReqDTO.setSeatNumber(Arrays.asList(22));

        Movie movie=new Movie();
        movie.setMovieId("123");
        movie.setMovieName("abc");
        movie.setReleaseDate(releaseDate);
        movie.setTheaterIdList(Arrays.asList(1));

        Mockito.when(movieRepo.findById("123")).thenReturn(Optional.of(movie));

        Theater theater=new Theater();
        theater.setTheaterName("t1");
        theater.setTheaterLoc("tloc1");
        theater.setTheaterId(1);
        theater.setTheaterCapacity(100);
        theater.setBookedTickets(new HashSet<>(Arrays.asList(20,21)));
        Mockito.when(theaterRepo.findById(1)).thenReturn(Optional.of(theater));

        Mockito.when(ticketRepo.count()).thenReturn((long)1);

        String userId = "";
        Assertions.assertEquals(HttpStatus.OK,movieService.bookTicket(demoBookTicketReqDTO, userId).getStatusCode());
    }
}