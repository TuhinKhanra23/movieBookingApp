package com.cts.fse.service;

import org.springframework.http.ResponseEntity;

public interface IAdminService {
    ResponseEntity<String> updateSeatStatus(Integer theaterId);
}
