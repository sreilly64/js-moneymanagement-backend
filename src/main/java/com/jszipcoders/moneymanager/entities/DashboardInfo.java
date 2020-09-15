package com.jszipcoders.moneymanager.entities;

import javax.persistence.Column;
import java.util.List;

public class DashboardInfo {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String email;
    private final String phoneNumber;
    private final String username;
    private final List<AccountEntity> accounts;

    public DashboardInfo(UserEntity user, List<AccountEntity> accounts){
        this(user.getFirstName(), user.getLastName(), user.getAddress(), user.getEmail(), user.getPhoneNumber(), user.getUsername(), accounts);
    }

    public DashboardInfo(String firstName, String lastName, String address, String email, String phoneNumber, String username, List<AccountEntity> accounts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.accounts = accounts;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }
}
