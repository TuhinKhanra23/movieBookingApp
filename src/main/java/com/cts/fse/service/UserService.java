package com.cts.fse.service;


import com.cts.fse.model.User;
import com.cts.fse.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepo userRepo;

//    @Autowired
//    MongoConnectionConfig mongoConnectionConfig;


    @Override
    public ResponseEntity<String> saveUser(User user) {
        Optional<User> userInLedger = userRepo.findById(user.getLoginId());
        List<User> userPresent = userRepo.findByEmail(user.getEmail());
        if (userInLedger.isPresent()) {
            return new ResponseEntity<>("Login Id Already exits!", HttpStatus.CONFLICT);

        } else if (!userPresent.isEmpty() && userPresent.get(0).getEmail().equals(user.getEmail())) {
            return new ResponseEntity<>("Email Id Already exits!", HttpStatus.CONFLICT);
        } else {
            user.setUserActive(true);
            user.setRole("user");
            userRepo.save(user);
            return new ResponseEntity<>("Registration Successful", HttpStatus.OK);
        }
//        else if(userInLedger.get().getEmail().equals(user.getEmail())) {
//            return new ResponseEntity<>("Login Id Already exits!", HttpStatus.CONFLICT);
//        MongoCollection<Document> userCollection=mongoConnectionConfig.getConnection("user");
//        if(!Objects.requireNonNull(userCollection.find(new Document("_id", user.getLoginId())).first()).isEmpty()){
//            return new ResponseEntity<>("Login Id Already exits!", HttpStatus.CONFLICT);
//
//        }
//        userCollection.drop();
//        return new ResponseEntity<>("Registration Successful", HttpStatus.OK);
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

//                user.setPassword(userInLedger.get().getPassword());
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
