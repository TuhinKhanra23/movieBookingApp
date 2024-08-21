package com.cts.fse.service;


import com.cts.fse.config.MongoConnectionConfig;
import com.cts.fse.dto.ShowTicketResDto;
import com.cts.fse.dto.UserRegisterDTO;
import com.cts.fse.model.Movie;
import com.cts.fse.model.Theater;
import com.cts.fse.model.Ticket;
import com.cts.fse.model.User;
import com.cts.fse.repository.MovieRepo;
import com.cts.fse.repository.TheaterRepo;
import com.cts.fse.repository.TicketRepo;
import com.cts.fse.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    MongoConnectionConfig mongoConnectionConfig;
    @Autowired
    TicketRepo ticketRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MovieRepo movieRepo;
    @Autowired
    private TheaterRepo theaterRepo;

    @Override
    public ResponseEntity<String> saveUser(UserRegisterDTO user) {

        Optional<?> userInLedger = userRepo.findById(user.getLoginId());
        if (userInLedger.isPresent()) {
            return new ResponseEntity<>("Login Id Already exits!", HttpStatus.CONFLICT);

        } else if (!userRepo.findByEmail(user.getEmail()).isEmpty()) {
            return new ResponseEntity<>("Email Id Already exits!", HttpStatus.CONFLICT);

        } else {
            User userDetails = new User();
            userDetails.setUserActive(true);
            userDetails.setRole("user");
            userDetails.setName(user.getName());
            userDetails.setPassword(user.getPassword());
            userDetails.setEmail(user.getEmail());
            userDetails.setLoginId(user.getLoginId());
            userDetails.setContactNo(user.getContactNo());
            userRepo.save(userDetails);
            return new ResponseEntity<>("Registration Successful", HttpStatus.OK);
        }

    }


    @Override
    public ResponseEntity<User> userLogin(String loginId, String password) {
        Optional<User> userInLedger = userRepo.findById(loginId);
        User user = new User();
        if (userInLedger.isPresent()) {
            if (userInLedger.get().getPassword().equals(password)) {
                user.setLoginId(userInLedger.get().getLoginId());
                user.setEmail(userInLedger.get().getEmail());
                user.setName(userInLedger.get().getName());
                user.setContactNo(userInLedger.get().getContactNo());

                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> resetPassword(String loginId, String newPassword) {
        Optional<User> userInLedger = userRepo.findById(loginId);
        User user = new User();
        if (userInLedger.isPresent()) {
            user.setLoginId(userInLedger.get().getLoginId());
            user.setEmail(userInLedger.get().getEmail());
            user.setName(userInLedger.get().getName());
            user.setContactNo(userInLedger.get().getContactNo());
            System.out.println(newPassword);
            user.setPassword(newPassword);
            System.out.println(user);
            user.setUserActive(true);
            user.setRole("user");
            if (null != userInLedger.get().getBookedTicket()) {
                user.setBookedTicket(userInLedger.get().getBookedTicket());
            }
            userRepo.save(user);

            return new ResponseEntity<>("Password Changed", HttpStatus.OK);
        }
        return new ResponseEntity<>("UserID not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<ShowTicketResDto> showBookedTickets(String userId) {
        List<Ticket> ticketList = ticketRepo.findByUserId(userId);
        List<ShowTicketResDto> showTicketResDtoList = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            if (!ticketList.isEmpty()) {

                ShowTicketResDto showTicketResDto = new ShowTicketResDto();

                Optional<User> user = userRepo.findById(userId);
                user.ifPresent(value -> showTicketResDto.setName(value.getName()));
                Optional<Movie> movie = movieRepo.findById(ticket.getMovieId());
                movie.ifPresent(value -> showTicketResDto.setMovieName(value.getMovieName()));

                Optional<Theater> theater = theaterRepo.findById(ticket.getTheaterId());
                if (theater.isPresent()) {
                    showTicketResDto.setTheaterName(theater.get().getTheaterName());
                    showTicketResDto.setTheaterLoc(theater.get().getTheaterLoc());
                }
                showTicketResDto.setBookingDate(ticket.getBookingDate());
                showTicketResDto.setSeatNumber(ticket.getSeatNumber());

                showTicketResDtoList.add(showTicketResDto);
            }
        }

        return showTicketResDtoList;
    }


}
