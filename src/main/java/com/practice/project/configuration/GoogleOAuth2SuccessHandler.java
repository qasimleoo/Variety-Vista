package com.practice.project.configuration;

import com.practice.project.modal.Role;
import com.practice.project.modal.User;
import com.practice.project.repository.RoleRepo;
import com.practice.project.repository.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    UserRepo userRepo;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String email = token.getPrincipal().getAttributes().get("email").toString();
        if(userRepo.findUserByEmail(email).isPresent()){
        }
        else {
            User user = new User();
            user.setFirstName(token.getPrincipal().getAttributes().get("given_name").toString());

            if (token.getPrincipal().getAttributes().containsKey("family_name")) {
                user.setLastName(token.getPrincipal().getAttributes().get("family_name").toString());
            }

            user.setEmail(token.getPrincipal().getAttributes().get("email").toString());

            List<Role> roles = new ArrayList<>();
            roles.add(roleRepo.findById(2).get());
            user.setRoles(roles);

            userRepo.save(user);
        }
        redirectStrategy.sendRedirect(request, response, "/");
    }
}
