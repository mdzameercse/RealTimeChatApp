package com.zameer.ChatApp.Service;

import com.zameer.ChatApp.Model.Friend;
import com.zameer.ChatApp.Model.User;
import com.zameer.ChatApp.Repository.FriendRepo;
import com.zameer.ChatApp.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    @Autowired
    private FriendRepo friendRepo;
    @Autowired
    private UserRepo userRepo;

    public List<Friend> getFriendsOfUser(User currentUser) {
        return friendRepo.findByUser(currentUser);
    }

    public String addFriend(User currentUser, User friendUser) {
        Friend friend = new Friend();
        friend.setUser(currentUser);
        friend.setFriend(friendUser);
        friendRepo.save(friend);
     return "Successfylly Add Friend";
    }
}
