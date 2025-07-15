package com.zameer.ChatApp.Config;

import com.zameer.ChatApp.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
class CustomLogoutHandler implements LogoutSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            userService.updateUserStatus(username, false);
        }
    }
}
