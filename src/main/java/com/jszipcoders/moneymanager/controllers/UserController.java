package com.jszipcoders.moneymanager.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jszipcoders.moneymanager.entities.AuthenticationRequest;
import com.jszipcoders.moneymanager.entities.AuthenticationResponse;
import com.jszipcoders.moneymanager.entities.UserEntity;
import com.jszipcoders.moneymanager.services.UserService;
import com.jszipcoders.moneymanager.util.JwtUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> addUser(@RequestBody UserEntity newUser){
        UserEntity userEntity = null;
        try{
            userEntity = userService.addUser(newUser);
        }catch (Exception e){
            LOGGER.info(e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<UserEntity>(userEntity, HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/{user_id}")
    public ResponseEntity<UserEntity> findUserById(@PathVariable Long user_id){
        UserEntity userEntity = null;
        try{
            userEntity = userService.findUserById(user_id);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
    }

    @PutMapping(value = "/users/{user_id}")
    public ResponseEntity<?> updateUserDetails(@PathVariable Long user_id, @RequestBody UserEntity updatedUser){
        UserEntity userEntity = null;
        try{
            userEntity = userService.updateUserDetails(user_id, updatedUser);
        }catch (IllegalArgumentException e){
            LOGGER.info(e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{user_id}")
    public ResponseEntity<Boolean> deleteUserById(@PathVariable Long user_id){
        Boolean userDeleted = null;
        try{
            userDeleted = userService.deleteUserById(user_id);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Boolean>(userDeleted, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
//            );
//        } catch (BadCredentialsException e){
//            throw new Exception("Incorrect username or password", e);
//        }
        LOGGER.info("Username: " + authRequest.getUsername());
        LOGGER.info("Password: " + authRequest.getPassword());

        UserDetails user = null;
        try{
            user = userService.userLogin(authRequest.getUsername(), authRequest.getPassword());
        } catch (AuthenticationException e){
            String message = e.getMessage();
            JSONObject json = new JSONObject();
            json.put("message", message);

            LOGGER.info(e.getMessage(), e);
            return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        String jwt = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
