package com.cts.fse.service;

import com.cts.fse.dto.ShowTicketResDto;
import com.cts.fse.dto.UserRegisterDTO;
import com.cts.fse.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    public ResponseEntity<String> saveUser(UserRegisterDTO user);

    public ResponseEntity<User> userLogin(String loginId, String password);

    ResponseEntity<String> resetPassword(String loginId, String newPassword);

    List<ShowTicketResDto> showBookedTickets(String userId);
}
