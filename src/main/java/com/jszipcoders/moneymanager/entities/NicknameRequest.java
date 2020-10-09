package com.jszipcoders.moneymanager.entities;

public class NicknameRequest {

    private String nickname;

    public NicknameRequest() {
    }

    public NicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
