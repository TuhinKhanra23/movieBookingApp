package com.cts.fse.service;

import com.cts.fse.exception.MovieBookingException;
import org.springframework.http.ResponseEntity;

public interface IAdminService {
    ResponseEntity<String> updateSeatStatus(Integer theaterId) throws MovieBookingException;
}
