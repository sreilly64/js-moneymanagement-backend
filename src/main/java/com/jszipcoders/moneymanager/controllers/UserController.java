package com.jszipcoders.moneymanager.controllers;

import com.jszipcoders.moneymanager.responses.AuthenticationResponse;
import com.jszipcoders.moneymanager.requests.PasswordRequest;
import com.jszipcoders.moneymanager.entities.UserEntity;
import com.jszipcoders.moneymanager.services.UserService;
import com.jszipcoders.moneymanager.util.JwtUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> addUser(@RequestBody UserEntity newUser){
        UserEntity userEntity = null;
        try{
            userEntity = userService.addUser(newUser);
        }catch (Exception e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        String jwt = jwtUtil.generateToken(userEntity);
        AuthenticationResponse response = new AuthenticationResponse(jwt, userEntity.getUserId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<UserEntity> findUserById(@PathVariable Long userId){
        UserEntity userEntity = null;
        try{
            userEntity = userService.findUserById(userId);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @PutMapping(value = "/users/{userId}")
    public ResponseEntity<?> updateUserDetails(@PathVariable Long userId, @RequestBody UserEntity updatedUser){
        UserEntity userEntity = null;
        try{
            userEntity = userService.updateUserDetails(userId, updatedUser);
        }catch (IllegalArgumentException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @PutMapping(value = "/users/{userId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestBody PasswordRequest request){
        UserEntity userEntity = null;
        try{
            userEntity = userService.updatePassword(userId, request);
        }catch (BadCredentialsException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping(value = "/users/{userId}")
    public ResponseEntity<Boolean> deleteUserById(@PathVariable Long userId){
        boolean userDeleted = userService.deleteUserById(userId);
        if(userDeleted){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.badRequest().body(false);
        }
    }

}
