package com.jszipcoders.moneymanager.services;

import com.jszipcoders.moneymanager.requests.PasswordRequest;
import com.jszipcoders.moneymanager.entities.UserEntity;
import com.jszipcoders.moneymanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserService implements UserDetailsService{

    private final UserRepository userRepo;

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

    public UserEntity findUserById(Long userId) {
        return userRepo.findById(userId).get();
    }

    public UserEntity updateUserDetails(Long userId, UserEntity updatedUser) {
        UserEntity userEntity = userRepo.findById(userId).get();

        userEntity.setFirstName(updatedUser.getFirstName());
        userEntity.setLastName(updatedUser.getLastName());
        userEntity.setAddress(updatedUser.getAddress());
        userEntity.setEmail(updatedUser.getEmail());
        userEntity.setPhoneNumber(updatedUser.getPhoneNumber());

        checkIfUserDetailsAreTaken(userEntity);

        return userRepo.save(userEntity);
    }

    public Boolean deleteUserById(Long userId) {
        if(userRepo.existsById(userId)){
            UserEntity userEntity = userRepo.getOne(userId);
            userRepo.delete(userEntity);
            return true;
        }else{
            return false;
        }
    }

    public UserEntity userLogin(String username, String password) {
        List<UserEntity> users = userRepo.findAll();
        for(UserEntity user: users){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return user;
            }
        }
        throw new BadCredentialsException("Invalid username or password");
    }

    public Long getUserIdByUsername(String username) {
        List<UserEntity> users = userRepo.findAll();
        for(UserEntity user: users){
            if(user.getUsername().equals(username)){
                return user.getUserId();
            }
        }
        throw new UsernameNotFoundException("Invalid username");
    }

    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        List<UserEntity> users = userRepo.findAll();
        for(UserEntity user: users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        throw new UsernameNotFoundException("Invalid username");
    }

    public Boolean confirmPassword(String username, String password) {
        Long userId = getUserIdByUsername(username);
        UserEntity user = findUserById(userId);
        return user.getPassword().equals(password);
    }

    public UserEntity updatePassword(Long userId, PasswordRequest request) {
        UserEntity user = findUserById(userId);
        if(request.getOldPassword().equals(user.getPassword())){
            user.setPassword(request.getNewPassword());
            return userRepo.save(user);
        }else {
            throw new BadCredentialsException("Current password is invalid");
        }

    }
}
