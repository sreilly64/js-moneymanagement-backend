package com.jszipcoders.moneymanager.controllers;

import com.jszipcoders.moneymanager.entities.AuthenticationRequest;
import com.jszipcoders.moneymanager.entities.AuthenticationResponse;
import com.jszipcoders.moneymanager.entities.PasswordRequest;
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

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    private JwtUtil jwtUtil;

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
            return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        String jwt = jwtUtil.generateToken(userEntity);
        AuthenticationResponse response = new AuthenticationResponse(jwt, userEntity.getUserId());

        return new ResponseEntity<AuthenticationResponse>(response, HttpStatus.CREATED);
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
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
    }

    @PutMapping(value = "/users/{user_id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long user_id, @RequestBody PasswordRequest request){
        UserEntity userEntity = null;
        try{
            userEntity = userService.updatePassword(user_id, request);
        }catch (BadCredentialsException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userEntity);
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

}
