package com.zameer.ChatApp.Controller;

import com.zameer.ChatApp.Model.Friend;
import com.zameer.ChatApp.Model.User;
import com.zameer.ChatApp.Service.FriendService;
import com.zameer.ChatApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("friend")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserService userService;
    @GetMapping("/showall")
    public List<Friend> getFriends(Principal principal) {
        User currentUser =userService.findUserByUsername(principal.getName());
         List<Friend> friendList=friendService.getFriendsOfUser(currentUser);
         return friendList;
    }
   @PostMapping("addfriend/{fid}")
    public String addFriend(@PathVariable Integer fid,Principal principal){
       User currentUser = userService.findByUsername(principal.getName());
       User friendUser = userService.findByUsernameID(fid);
      return friendService.addFriend(currentUser,friendUser);
   }
}
