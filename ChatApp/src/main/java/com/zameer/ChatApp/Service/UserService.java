package com.zameer.ChatApp.Service;

import com.zameer.ChatApp.Config.SecutrityConfigg;
import com.zameer.ChatApp.Model.Friend;
import com.zameer.ChatApp.Model.User;
import com.zameer.ChatApp.Repository.FriendRepo;
import com.zameer.ChatApp.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    SecutrityConfigg secutrityConfigg;
    @Autowired
    FriendRepo  friendRepo;

    public String AddNewUser(User user) {
        user.setPassword(secutrityConfigg.passwordEncoder().encode(user.getPassword()));
        userRepo.save(user);
        return "Successfully added user";
    }

    public User findUserByUsername(String name) {
        return userRepo.findUserByUsername(name);
    }

    public User findByUsername(String name) {
        return userRepo.findUserByUsername(name);
    }

    public Optional<User> findUserByUserId(Integer fid) {
        return userRepo.findById(fid);
    }

    public User findByUsernameID(Integer fid) {
        return userRepo.findById(fid).get();
    }

    public List<User> showAllUsers() {
        return userRepo.findAll();
    }
    public void updateUserStatus(String username, boolean isOnline) {
        User user = userRepo.findUserByUsername(username);
        user.setOnline(isOnline);
        if (!isOnline) {
            user.setLastSeen(LocalDateTime.now());
        }
        userRepo.save(user);
    }

    public boolean existsByUsername(String username) {
        User user = userRepo.findUserByUsername(username);
        if (user != null) {return true;}
        return false;
    }

    public void addNewUser(User user) {
        user.setPassword(secutrityConfigg.passwordEncoder().encode(user.getPassword()));
        userRepo.save(user);
    }

    public List<User> getUsersNotFriends(String currentUsername) {
        // Find current user
        User currentUser = userRepo.findUserByUsername(currentUsername);

        // Get all friends of current user
        List<Friend> friends = friendRepo.findByUser(currentUser);

        // Extract friend IDs
        Set<Integer> friendIds = friends.stream()
                .map(friend -> friend.getFriend().getId())
                .collect(Collectors.toSet());

        // Add current user's own ID to exclude
        friendIds.add(currentUser.getId());

        // Fetch all users and filter
        return userRepo.findAll().stream()
                .filter(user -> !friendIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public LocalDateTime getLastSeen(String username) {
        User user = userRepo.findUserByUsername(username);
        return user.getLastSeen(); // Make sure `lastSeen` field exists in User entity
    }

    public void updateLastSeen(String username) {
        User user = userRepo.findUserByUsername(username);
        user.setLastSeen(LocalDateTime.now());
        userRepo.save(user);
    }

}
