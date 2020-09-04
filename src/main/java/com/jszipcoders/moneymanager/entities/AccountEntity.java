package com.jszipcoders.moneymanager.entities;

import javax.persistence.*;

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

    public AccountEntity() {
        this(null, null, null, null);
    }

    public AccountEntity(Long accountNumber, AccountType type, Long userId, Double balance) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.userId = userId;
        this.balance = balance;
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
}
