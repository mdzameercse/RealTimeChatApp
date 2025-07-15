package com.zameer.ChatApp.Config;

import com.zameer.ChatApp.Model.User;
import com.zameer.ChatApp.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImp implements UserDetailsService {
    @Autowired
    UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findUserByUsername(username);
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        if(user==null){
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetail(user);
    }
}
