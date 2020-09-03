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
            userEntity.setAccounts(updatedUser.getAccounts());
            return userRepo.save(userEntity);
        }else{
            return userEntity;
        }
    }

    public UserEntity deleteUserById(Long user_id) {
        UserEntity userEntity = userRepo.findById(user_id).get();
        userRepo.deleteById(user_id);
        return userEntity;
    }
}
