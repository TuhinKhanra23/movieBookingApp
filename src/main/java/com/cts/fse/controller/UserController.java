package com.cts.fse.controller;

import com.cts.fse.dto.ShowTicketResDto;
import com.cts.fse.dto.UserRegisterDTO;
import com.cts.fse.model.User;
import com.cts.fse.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {

    @Autowired
    private IUserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody UserRegisterDTO user) {
       return userService.saveUser(user);
    }

    @GetMapping("/login")
    public ResponseEntity<User> userLogin(@RequestParam(required = true) String loginId,
                                          @RequestParam(required = true) String password) {
        return userService.userLogin(loginId,password);
    }

    @PostMapping("/forgot")
    public ResponseEntity<String> resetPassword(@RequestParam(required = true) String loginId, @RequestParam(required = true) String newPassword) {
        return userService.resetPassword(loginId,newPassword);
    }

    @GetMapping("/showBookedTickets")
    public List<ShowTicketResDto> showBookedTickets(@RequestParam(required = true) String userId) {
        return userService.showBookedTickets(userId);
    }

}
