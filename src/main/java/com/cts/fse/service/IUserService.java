package com.cts.fse.service;

import com.cts.fse.model.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    public ResponseEntity<String> saveUser(User user);

    public ResponseEntity<User> userLogin(String loginId, String password);

    ResponseEntity<String> resetPassword(String loginId, String newPassword);
}
