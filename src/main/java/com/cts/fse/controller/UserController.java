package com.cts.fse.controller;

import com.cts.fse.model.User;
import com.cts.fse.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@RestController
public class UserController {

    @Autowired
    private IUserService userService;


    @PostMapping("/registerUser")
    public ResponseEntity<String> addUser(@RequestBody User user) {
       return userService.saveUser(user);
    }

    @GetMapping("/userLogin")
    public ResponseEntity<User> userLogin(@RequestParam String loginId, String password){
        return userService.userLogin(loginId,password);
    }

    @PostMapping("resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String loginId, String newPassword){
        return userService.resetPassword(loginId,newPassword);
    }

}
