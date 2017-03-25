package com.sjsu.model;


public class DigitalWallet {
    private double totalAmount;
    private double amountOnHold;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getAmountOnHold() {
        return amountOnHold;
    }

    public void setAmountOnHold(double amountOnHold) {
        this.amountOnHold = amountOnHold;
    }

    public void creditAmount(double amountToCredit) {
        totalAmount = totalAmount + amountToCredit;
    }

    public void debitAmount(double amountToDebit) throws InsufficientBalanceException {
        if (totalAmount > amountToDebit) {
            totalAmount = totalAmount - amountToDebit;
        } else {
            throw new InsufficientBalanceException("Insufficient balance in you account. Trying to debit $" + amountToDebit + " " +
                    "but you have $" + totalAmount + " available in your account");
        }
    }
}
