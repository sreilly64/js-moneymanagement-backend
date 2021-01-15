package com.jszipcoders.moneymanager.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "accountentity", schema = "public")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountnumber", columnDefinition = "serial")
    private Long accountNumber;

    @Column(name = "type")
    private AccountType type;

    @Column(name = "userid")
    private Long userId;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "routingnumber")
    private final Integer routingNumber = 394058927;

    @Column(name = "nickname")
    private String nickname;

    public AccountEntity() {
        this(null, null, null, null, null);
    }

    public AccountEntity(Long accountNumber, AccountType type, Long userId, Double balance) {
        this(accountNumber, type, userId, balance, null);
    }

    public AccountEntity(Long accountNumber, AccountType type, Long userId, Double balance, String nickname) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.userId = userId;
        this.balance = balance;
        this.nickname = nickname;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getRoutingNumber() {
        return routingNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return accountNumber.equals(that.accountNumber) &&
                type == that.type &&
                userId.equals(that.userId) &&
                balance.equals(that.balance) &&
                routingNumber.equals(that.routingNumber) &&
                Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, type, userId, balance, routingNumber, nickname);
    }
}
