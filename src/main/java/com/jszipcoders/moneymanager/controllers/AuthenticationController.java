package com.jszipcoders.moneymanager.controllers;

import com.jszipcoders.moneymanager.repositories.entities.*;
import com.jszipcoders.moneymanager.controllers.requests.AuthenticationRequest;
import com.jszipcoders.moneymanager.controllers.responses.AuthenticationResponse;
import com.jszipcoders.moneymanager.controllers.responses.DashboardInfo;
import com.jszipcoders.moneymanager.services.AccountService;
import com.jszipcoders.moneymanager.services.UserService;
import com.jszipcoders.moneymanager.util.JwtUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(UserService userService, AccountService accountService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.accountService = accountService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(value = "/users/{userId}/accounts")
    public ResponseEntity<DashboardInfo> getUserAndAccountsByUserId(@PathVariable Long userId){
        UserEntity user = null;
        try{
            user = userService.findUserById(userId);
        }catch (NoSuchElementException e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        List<AccountEntity> accounts = accountService.findAllAccountsByUserId(userId);
        DashboardInfo info = new DashboardInfo(user, accounts);
        return ResponseEntity.ok(info);
    }

    @PostMapping(value = "/authenticate/password")
    public ResponseEntity<?> confirmPassword(@RequestBody AuthenticationRequest authRequest) {
        Boolean validPassword = null;
        try{
            validPassword = userService.confirmPassword(authRequest.getUsername(), authRequest.getPassword());
        } catch (AuthenticationException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(json.toString());
        }
        return ResponseEntity.ok(validPassword);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authRequest) {
        UserDetails user = null;
        try{
            user = userService.userLogin(authRequest.getUsername(), authRequest.getPassword());
        } catch (AuthenticationException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(json.toString());
        }
        String jwt = jwtUtil.generateToken(user);
        AuthenticationResponse response = new AuthenticationResponse(jwt, userService.getUserIdByUsername(user.getUsername()));

        return ResponseEntity.ok(response);
    }
}
