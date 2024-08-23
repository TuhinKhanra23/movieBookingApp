package com.cts.fse.service;


import com.cts.fse.config.MongoConnectionConfig;
import com.cts.fse.dto.ShowTicketResDto;
import com.cts.fse.dto.UserRegisterDTO;
import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.model.Movie;
import com.cts.fse.model.Theater;
import com.cts.fse.model.Ticket;
import com.cts.fse.model.User;
import com.cts.fse.repository.MovieRepo;
import com.cts.fse.repository.TheaterRepo;
import com.cts.fse.repository.TicketRepo;
import com.cts.fse.repository.UserRepo;
import com.cts.fse.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements IUserService {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    MongoConnectionConfig mongoConnectionConfig;
    @Autowired
    TicketRepo ticketRepo;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtutil;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MovieRepo movieRepo;
    @Autowired
    private TheaterRepo theaterRepo;

    @Override
    public ResponseEntity<String> saveUser(UserRegisterDTO user) throws MovieBookingException {
        log.info("Inside SaveUser Service Method :");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<?> userInLedger = userRepo.findById(user.getLoginId());
        if (userInLedger.isPresent()) {
            log.info("Login Id Already exits! {}", user.getLoginId());
            throw new MovieBookingException("Login Id Already exits!");


        } else if (!userRepo.findByEmail(user.getEmail()).isEmpty()) {
            log.info("Email Id Already exits! {}", user.getEmail());
            throw new MovieBookingException("Email Id Already exits!");


        } else {
            User userDetails = new User();
            userDetails.setUserActive(true);
            userDetails.setRole("user");
            userDetails.setName(user.getName());
            userDetails.setPassword(encoder.encode(user.getPassword()));

            userDetails.setEmail(user.getEmail());
            userDetails.setLoginId(user.getLoginId());
            userDetails.setContactNo(user.getContactNo());
            userRepo.save(userDetails);
            log.info("Registration Successful!.. User Added to Database with Id {}", user.getLoginId());
            return new ResponseEntity<>("Registration Successful", HttpStatus.OK);
        }

    }


    @Override
    public ResponseEntity<User> userLogin(String loginId, String password) throws MovieBookingException {
//       Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//       String userName=authentication.getName();
        log.info("Inside userLogin Service Method!");
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);

        Optional<User> userInLedger = userRepo.findById(loginId);
        User user = new User();
        String generateToken = "";

        if (userInLedger.isPresent()) {

            if (passwordEncoder.matches(password, userInLedger.get().getPassword())) {
                log.info("Password Matched for user{}", loginId);
                generateToken = jwtutil.generateToken(userDetails);
                log.info("JWT Token is: {}", generateToken);
                user.setLoginId(userInLedger.get().getLoginId());
                user.setEmail(userInLedger.get().getEmail());
                user.setName(userInLedger.get().getName());
                user.setContactNo(userInLedger.get().getContactNo());
                user.setToken(generateToken);
                log.info("Login Successful!.. for userId {}", loginId);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }
        log.info("Login Failed!.. for userId {}", loginId);
        throw new MovieBookingException("Login Failed ..UserId or Password is Incorrect!!");
    }

    @Override
    public ResponseEntity<String> resetPassword(String loginId, String newPassword) throws MovieBookingException {
        log.info("Inside resetPassword Service");
        Optional<User> userInLedger = userRepo.findById(loginId);
        User user = new User();
        if (userInLedger.isPresent()) {
            user.setLoginId(userInLedger.get().getLoginId());
            user.setEmail(userInLedger.get().getEmail());
            user.setName(userInLedger.get().getName());
            user.setContactNo(userInLedger.get().getContactNo());

            user.setPassword(passwordEncoder.encode(newPassword));

            user.setUserActive(true);
            user.setRole("user");
            if (null != userInLedger.get().getBookedTicket()) {
                user.setBookedTicket(userInLedger.get().getBookedTicket());
            }
            userRepo.save(user);
            log.info("Password Change Successful ..for userId{}", loginId);
            return new ResponseEntity<>("Password Changed", HttpStatus.OK);
        }
        log.info("UserID not found{}", loginId);
        throw new MovieBookingException("UserId not found!!");
    }

    @Override
    public List<ShowTicketResDto> showBookedTickets(String userId) {
        log.info("Inside showBookedTickets Service");
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
                log.info("Ticket Added to response with ticketId{}", ticket.getTicketId());
                showTicketResDtoList.add(showTicketResDto);
            }
        }
        log.info("Returning Tickets List for no of Records:{}", showTicketResDtoList.size());
        return showTicketResDtoList;
    }


}
