package com.jszipcoders.moneymanager.entities;

import javax.persistence.Column;
import java.util.Arrays;
import java.util.List;

public class DashboardInfo {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final String username;
    private final String street;
    private final String city;
    private final String state;
    private final String zip;
    private final List<AccountEntity> accounts;


    public DashboardInfo(UserEntity user, List<AccountEntity> accounts){
        //this(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getUsername(), accounts);
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.username = user.getUsername();
        String[] address = getAddressComponents(user.getAddress());
        this.street = address[0];
        this.city = address[1];
        this.state = address[2];
        this.zip = address[3];
        this.accounts = accounts;
    }

    public String[] getAddressComponents(String address) {
        String[] output = new String[4];
        String[] addressComponents = address.split(",");
        for(int i = 0; i < addressComponents.length; i++){
            addressComponents[i] = addressComponents[i].trim();
        }
        output[0] = addressComponents[0];
        output[1] = addressComponents[1];
        output[2] = addressComponents[2].split(" ")[0];
        output[3] = addressComponents[2].split(" ")[1];

        return output;
    }

    public DashboardInfo(String firstName, String lastName, String email, String phoneNumber, String username, String street, String city, String state, String zip, List<AccountEntity> accounts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.accounts = accounts;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }
}
