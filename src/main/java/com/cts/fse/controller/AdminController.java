package com.cts.fse.controller;

import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.service.IAdminService;
import com.cts.fse.utils.JwtUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    JwtUtil jwtUtil;

    @ApiOperation(value = "Update Movie Seats", response = ResponseEntity.class)
    @PostMapping("/update")
    public ResponseEntity<String> updateSeatStatus(@RequestHeader String token, @RequestParam(required = true) Integer theaterId) throws MovieBookingException {
        log.info("Inside /update Api Controller for theater{}", theaterId);
        if (jwtUtil.validateToken(token)) {
            log.info("JWT Token validated ");
            return adminService.updateSeatStatus(theaterId);
        } else {
            throw new MovieBookingException("Invalid Token");
        }
    }
}
