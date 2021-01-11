package com.jszipcoders.moneymanager.responses;

public class TransactionResponse {

    private final String transactionType;
    private final Double dollarAmount;
    private final Boolean overDrafted;
    private final String message;

    public TransactionResponse(String transactionType, Double dollarAmount, Boolean overDrafted) {
        this.transactionType = transactionType;
        this.dollarAmount = dollarAmount;
        this.overDrafted = overDrafted;
        this.message = null;
    }

    public TransactionResponse(String message) {
        this.transactionType = null;
        this.dollarAmount = null;
        this.overDrafted = null;
        this.message = message;
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

    public String getMessage() {
        return message;
    }
}
