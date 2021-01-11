package com.jszipcoders.moneymanager.dto;

import com.jszipcoders.moneymanager.entities.AccountType;

import java.util.Objects;

public class AccountDTO {

    private final AccountType type;
    private final Long userId;
    private final Double balance;
    private final String nickname;

    public AccountDTO() {
        this(null, null, null, null);
    }

    public AccountDTO(AccountType type, Long userId, Double balance, String nickname) {
        this.type = type;
        this.userId = userId;
        this.balance = balance;
        this.nickname = nickname;
    }

    public AccountType getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }

    public Double getBalance() {
        return balance;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDTO that = (AccountDTO) o;
        return type == that.type &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(balance, that.balance) &&
                Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, userId, balance, nickname);
    }
}
