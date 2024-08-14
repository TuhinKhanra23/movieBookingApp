package com.cts.fse.service;


import com.cts.fse.config.MongoConnectionConfig;
import com.cts.fse.model.User;
import com.cts.fse.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    MongoConnectionConfig mongoConnectionConfig;


    @Override
    public ResponseEntity<String> saveUser(User user) {
//       MongoDatabase database= mongoConnectionConfig.establishConnection();
//        MongoCollection<User> userCollection=database.getCollection("user",User.class);
        Optional<?> userInLedger = userRepo.findById(user.getLoginId());
        if (userInLedger.isPresent()) {
            return new ResponseEntity<>("Login Id Already exits!", HttpStatus.CONFLICT);

        } else if (!userRepo.findByEmail(user.getEmail()).isEmpty()) {
            return new ResponseEntity<>("Email Id Already exits!", HttpStatus.CONFLICT);

        } else {
            user.setUserActive(true);
            user.setRole("user");
            userRepo.save(user);
//            userCollection.drop();
            return new ResponseEntity<>("Registration Successful", HttpStatus.OK);
        }

    }


    @Override
    public ResponseEntity<User> userLogin(String loginId, String password) {
        Optional<User> userInLedger = userRepo.findById(loginId);
        User user = new User();
        if (userInLedger.isPresent()) {
            if (userInLedger.get().getPassword().equals(password)) {
                user.setLoginId(userInLedger.get().getLoginId());
                user.setEmail(userInLedger.get().getEmail());
                user.setName(userInLedger.get().getName());
                user.setContactNo(userInLedger.get().getContactNo());

                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> resetPassword(String loginId, String newPassword) {
        Optional<User> userInLedger = userRepo.findById(loginId);
        User user = new User();
        if (userInLedger.isPresent()) {
            user.setLoginId(userInLedger.get().getLoginId());
            user.setEmail(userInLedger.get().getEmail());
            user.setName(userInLedger.get().getName());
            user.setContactNo(userInLedger.get().getContactNo());
            System.out.println(newPassword);
            user.setPassword(newPassword);
            System.out.println(user);
            userRepo.save(user);
            return new ResponseEntity<>("Password Changed", HttpStatus.OK);
        }
        return new ResponseEntity<>("UserID not found",HttpStatus.NOT_FOUND);
    }


}
