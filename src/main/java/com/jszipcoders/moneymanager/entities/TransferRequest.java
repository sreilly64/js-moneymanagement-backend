package com.jszipcoders.moneymanager.entities;

public class TransferRequest {

    private Long fromAccountId;
    private Long toAccountId;
    private Double dollarAmount;

    public TransferRequest(Long fromAccountId, Long toAccountId, Double dollarAmount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.dollarAmount = dollarAmount;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public Double getDollarAmount() {
        return dollarAmount;
    }

    public void setDollarAmount(Double dollarAmount) {
        this.dollarAmount = dollarAmount;
    }
}
