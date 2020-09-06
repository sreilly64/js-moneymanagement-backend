package com.jszipcoders.moneymanager.services;

import com.jszipcoders.moneymanager.entities.UserEntity;
import com.jszipcoders.moneymanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    public UserEntity addUser(UserEntity newUser) {
        checkIfUserDetailsAreTaken(newUser);
        return userRepo.save(newUser);
    }

    private void checkIfUserDetailsAreTaken(UserEntity userEntity){
        List<UserEntity> users = userRepo.findAll();

        if(userEntity.getUserId() != null){
            UserEntity oldUser = userRepo.findById(userEntity.getUserId()).orElse(null);
            users.remove(oldUser);
        }
        users.forEach(user -> {
            if(userEntity.getSsn().equals(user.getSsn())){
                throw new IllegalArgumentException("SSN already in use.");
            }else if(userEntity.getEmail().equals(user.getEmail())){
                throw new IllegalArgumentException("Email already in use.");
            }else if(userEntity.getUsername().equals(user.getUsername())){
                throw new IllegalArgumentException("Username already in use.");
            }else if(userEntity.getPhoneNumber().equals(user.getPhoneNumber())){
                throw new IllegalArgumentException("Phone number already in use.");
            }
        });
    }

    public UserEntity findUserById(Long user_id) {
        return userRepo.findById(user_id).get();
    }

    public UserEntity updateUserDetails(Long user_id, UserEntity updatedUser) {
        UserEntity userEntity = userRepo.findById(user_id).get();

        userEntity.setFirstName(updatedUser.getFirstName());
        userEntity.setLastName(updatedUser.getLastName());
        userEntity.setAddress(updatedUser.getAddress());
        userEntity.setEmail(updatedUser.getEmail());
        userEntity.setPhoneNumber(updatedUser.getPhoneNumber());
        userEntity.setPassword(updatedUser.getPassword());

        checkIfUserDetailsAreTaken(userEntity);

        return userRepo.save(userEntity);
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
