package com.example.basicbankingapp;

public class ViewTransactionDatatype {

    private String fromName;
    private String toName;
    private String amtTransferred;

    public ViewTransactionDatatype() {

    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getAmtTransferred() {
        return amtTransferred;
    }

    public void setAmtTransferred(String amtTransferred) {
        this.amtTransferred = amtTransferred;
    }
}
