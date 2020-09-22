package com.jszipcoders.moneymanager.controllers;

import com.jszipcoders.moneymanager.entities.*;
import com.jszipcoders.moneymanager.services.AccountService;
import com.jszipcoders.moneymanager.services.UserService;
import com.jszipcoders.moneymanager.util.JwtUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    private AccountService accountService;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(UserService userService, AccountService accountService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.accountService = accountService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(value = "/users/{userId}/accounts")
    public ResponseEntity<?> getUserAndAccountsByUserId(@PathVariable Long userId){
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
    public ResponseEntity<?> confirmPassword(@RequestBody AuthenticationRequest authRequest) throws Exception {
        Boolean validPassword = null;
        try{
            validPassword = userService.confirmPassword(authRequest.getUsername(), authRequest.getPassword());
        } catch (AuthenticationException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(validPassword);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
        UserDetails user = null;
        try{
            user = userService.userLogin(authRequest.getUsername(), authRequest.getPassword());
        } catch (AuthenticationException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        String jwt = jwtUtil.generateToken(user);
        AuthenticationResponse response = new AuthenticationResponse(jwt, userService.getUserIdByUsername(user.getUsername()));

        return ResponseEntity.ok(response);
    }
}
