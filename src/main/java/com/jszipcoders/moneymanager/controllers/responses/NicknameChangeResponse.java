package com.jszipcoders.moneymanager.controllers.responses;

import com.jszipcoders.moneymanager.controllers.requests.NicknameRequest;

public class NicknameChangeResponse {

    private final String nickname;
    private final String message;

    public NicknameChangeResponse() {
        this(null, null);
    }

    public NicknameChangeResponse(String errorMessage) {
        this(null, errorMessage);
    }

    public NicknameChangeResponse(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public NicknameChangeResponse(NicknameRequest request){
        this.nickname = request.getNickname();
        this.message = null;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMessage() {
        return message;
    }
}
