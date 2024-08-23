package com.cts.fse.service;

import com.cts.fse.model.User;
import com.cts.fse.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
      Optional<User> user=userRepo.findById(id);
      if(user.isPresent()){

          return org.springframework.security.core.userdetails.User.builder()
                  .username(user.get().getLoginId())
                  .password(user.get().getPassword())
                  .roles(user.get().getRole())
                  .build();
      }
      throw new UsernameNotFoundException("user not found");
    }
}
