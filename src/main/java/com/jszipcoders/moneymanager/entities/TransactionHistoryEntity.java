package com.jszipcoders.moneymanager.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class TransactionHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId", columnDefinition = "serial")
    private Long transactionId;
    @Column(name = "primaryAccount")
    private Long primaryAccountNumber;
    @Column(name = "secondaryAccount")
    private Long secondaryAccountNumber;
    @Column(name = "time")
    private LocalTime timestamp;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "dollarAmount")
    private Double dollarAmount;
    @Column(name = "transactionType")
    private TransactionType transactionType;

    public TransactionHistoryEntity() {
    }

    public TransactionHistoryEntity(Long primaryAccountNumber, Long secondaryAccountNumber, LocalDateTime dateTime, Double dollarAmount, TransactionType transactionType) {
        this.primaryAccountNumber = primaryAccountNumber;
        this.secondaryAccountNumber = secondaryAccountNumber;
        this.date = dateTime.toLocalDate();
        this.timestamp = dateTime.toLocalTime();
        this.dollarAmount = dollarAmount;
        this.transactionType = transactionType;
    }

    public TransactionHistoryEntity(Long transactionId, Long primaryAccountNumber, Long secondaryAccountNumber, LocalTime timestamp, LocalDate date, Double dollarAmount, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.primaryAccountNumber = primaryAccountNumber;
        this.secondaryAccountNumber = secondaryAccountNumber;
        this.timestamp = timestamp;
        this.date = date;
        this.dollarAmount = dollarAmount;
        this.transactionType = transactionType;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getPrimaryAccountNumber() {
        return primaryAccountNumber;
    }

    public void setPrimaryAccountNumber(Long primaryAccountNumber) {
        this.primaryAccountNumber = primaryAccountNumber;
    }

    public Long getSecondaryAccountNumber() {
        return secondaryAccountNumber;
    }

    public void setSecondaryAccountNumber(Long secondaryAccountNumber) {
        this.secondaryAccountNumber = secondaryAccountNumber;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getDollarAmount() {
        return dollarAmount;
    }

    public void setDollarAmount(Double dollarAmount) {
        this.dollarAmount = dollarAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionHistoryEntity that = (TransactionHistoryEntity) o;
        return Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(primaryAccountNumber, that.primaryAccountNumber) &&
                Objects.equals(secondaryAccountNumber, that.secondaryAccountNumber) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(dollarAmount, that.dollarAmount) &&
                transactionType == that.transactionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, primaryAccountNumber, secondaryAccountNumber, timestamp, dollarAmount, transactionType);
    }
}
