package com.example.smsbomber.classes;

public class Contact {
    private final String name;
    private final String telephone;
    private final int smsNumber;

    public Contact(String name, String telephone, int smsNumber) {
        this.name = name;
        this.telephone = telephone;
        this.smsNumber = smsNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public int getSmsNumber() {
        return this.smsNumber;
    }
}
