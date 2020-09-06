package com.jszipcoders.moneymanager.controllers;

import com.jszipcoders.moneymanager.entities.UserEntity;
import com.jszipcoders.moneymanager.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
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

}
