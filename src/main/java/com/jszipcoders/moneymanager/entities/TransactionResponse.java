package com.jszipcoders.moneymanager.entities;

public class TransactionResponse {

    private final String transactionType;
    private final Double dollarAmount;
    private final Boolean overDrafted;

    public TransactionResponse(String transactionType, Double dollarAmount, Boolean overDrafted) {
        this.transactionType = transactionType;
        this.dollarAmount = dollarAmount;
        this.overDrafted = overDrafted;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Double getDollarAmount() {
        return dollarAmount;
    }

    public Boolean getOverDrafted() {
        return overDrafted;
    }
}
