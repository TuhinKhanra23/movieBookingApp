package com.cts.fse.controller;

import com.cts.fse.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @PostMapping("/update")
    public ResponseEntity<String> updateSeatStatus(@RequestParam(required = true) Integer theaterId) {
        return adminService.updateSeatStatus(theaterId);
    }
}
