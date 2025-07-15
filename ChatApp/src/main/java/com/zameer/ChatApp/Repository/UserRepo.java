package com.zameer.ChatApp.Repository;

import com.zameer.ChatApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
    User findUserByUsername(String username);
}
