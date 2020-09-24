package com.jszipcoders.moneymanager.entities;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DashboardInfoTest {

    @Test
    public void getAddressComponents(){
        //given
        UserEntity user = new UserEntity();
        String address = "123 Street, Wilmington, Delaware 19803";
        user.setAddress(address);
        DashboardInfo info = new DashboardInfo(user, new ArrayList<AccountEntity>());
        String[] expected = {"123 Street", "Wilmington", "Delaware", "19803"};
        //when
        String[] actual = info.getAddressComponents(address);
        //then
        Assertions.assertArrayEquals(expected, actual);
    }

}