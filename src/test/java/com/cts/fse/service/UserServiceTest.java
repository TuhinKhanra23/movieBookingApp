package com.cts.fse.service;

import com.cts.fse.exception.MovieBookingException;
import com.cts.fse.model.Ticket;
import com.cts.fse.model.User;
import com.cts.fse.repository.UserRepo;
import com.cts.fse.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserDetailsServiceImpl userDetailsService;
    @Mock
    UserDetails userDetails;
    @Mock
    private UserRepo userRepo;
    @Mock
    private JwtUtil jwtutil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser() {
//        User demouser=new User("xyz","xyz@gmail.com","name","password","123",true,"user",demoticket);
//        Mockito.when(userRepo.findById("xyz")).thenReturn(Optional.of(demouser));

    }

    @Test
    void userLogin() throws MovieBookingException {
        Ticket demoticket=new Ticket("567","xyz","12",1,new Date(), Arrays.asList(2,3));
        User demouser=new User("xyz","xyz@gmail.com","name","password","123",true,"user",demoticket,"tokenab");
        Mockito.when(userDetailsService.loadUserByUsername("xyz")).thenReturn(userDetails);
        Mockito.when(userRepo.findById("xyz")).thenReturn(Optional.of(demouser));
        Mockito.when(jwtutil.generateToken(userDetails)).thenReturn("dummyToken");
        Assertions.assertThrows(MovieBookingException.class, () -> {
            userService.userLogin("xyz", "xyz");
        });
//        Assertions.assertEquals(HttpStatus.OK,userService.userLogin("xyz","xyz").getStatusCode());
    }

    @Test
    void resetPassword() {
    }

    @Test
    void showBookedTickets() {
    }
}