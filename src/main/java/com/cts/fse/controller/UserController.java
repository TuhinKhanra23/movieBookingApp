package com.cts.fse.controller;

import com.cts.fse.dto.ShowTicketResDto;
import com.cts.fse.dto.UserRegisterDTO;
import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.model.User;
import com.cts.fse.service.IUserService;
import com.cts.fse.utils.JwtUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    private IUserService userService;

    @ApiOperation(value = "Register User", response = ResponseEntity.class)
    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody UserRegisterDTO user) throws MovieBookingException {
        log.info("Inside /register Api Controller with UserId{}", user.getLoginId());
        return userService.saveUser(user);
    }

    @ApiOperation(value = "User Login", response = ResponseEntity.class)
    @GetMapping("/login")
    public ResponseEntity<User> userLogin(@RequestParam(required = true) String loginId,
                                          @RequestParam(required = true) String password) throws MovieBookingException {
        log.info("Inside /login Api Controller with UserId{}", loginId);
        return userService.userLogin(loginId, password);
    }

    @ApiOperation(value = "User Password Change", response = ResponseEntity.class)
    @PostMapping("/forgot")
    public ResponseEntity<String> resetPassword(@RequestParam(required = true) String loginId, @RequestParam(required = true) String newPassword) throws MovieBookingException {
        log.info("Inside /forgot Api Controller with UserId{}", loginId);
        return userService.resetPassword(loginId, newPassword);

    }

    @ApiOperation(value = "Show Booked Tickets", response = ResponseEntity.class)
    @GetMapping("/showBookedTickets")
    public List<ShowTicketResDto> showBookedTickets(@RequestHeader String token) throws MovieBookingException {
        log.info("Inside /showBookedTickets Api Controller ");
        if (jwtUtil.validateToken(token)) {

            String userId = jwtUtil.extractUsername(token);
            log.info("JWT Token validated for UserId{}", userId);
            return userService.showBookedTickets(userId);
        } else {
            throw new MovieBookingException("Invalid Token");
        }
    }

}
