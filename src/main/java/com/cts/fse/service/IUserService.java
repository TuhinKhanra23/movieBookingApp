package com.cts.fse.service;

import com.cts.fse.dto.ShowTicketResDto;
import com.cts.fse.dto.UserRegisterDTO;
import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    public ResponseEntity<String> saveUser(UserRegisterDTO user) throws MovieBookingException;

    public ResponseEntity<User> userLogin(String loginId, String password) throws MovieBookingException;

    ResponseEntity<String> resetPassword(String loginId, String newPassword) throws MovieBookingException;

    List<ShowTicketResDto> showBookedTickets(String userId);
}
