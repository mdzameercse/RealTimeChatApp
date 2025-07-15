package com.zameer.ChatApp.Repository;

import com.zameer.ChatApp.Model.Friend;
import com.zameer.ChatApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepo extends JpaRepository<Friend,Integer> {

    List<Friend> findByUser(User currentUser);
}
