package com.jszipcoders.moneymanager.services;

import com.jszipcoders.moneymanager.entities.UserEntity;
import com.jszipcoders.moneymanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    public UserEntity addUser(UserEntity newUser) {
        return userRepo.save(newUser);
    }

    public UserEntity findUserById(Long user_id) {
        return userRepo.findById(user_id).get();
    }

    public UserEntity updateUserDetails(Long user_id, UserEntity updatedUser) {
        UserEntity userEntity = userRepo.findById(user_id).get();
        if(userEntity != null){
            userEntity.setFirstName(updatedUser.getFirstName());
            userEntity.setLastName(updatedUser.getLastName());
            userEntity.setAddress(updatedUser.getAddress());
            userEntity.setEmail(updatedUser.getEmail());
            userEntity.setPhoneNumber(updatedUser.getPhoneNumber());
            userEntity.setPassword(updatedUser.getPassword());
            return userRepo.save(userEntity);
        }else{
            return userEntity;
        }
    }

    public Boolean deleteUserById(Long user_id) {
        UserEntity userEntity = userRepo.findById(user_id).orElse(null);
        userRepo.deleteById(user_id);
        if(userEntity != null){
            return true;
        }else{
            return false;
        }
    }
}
