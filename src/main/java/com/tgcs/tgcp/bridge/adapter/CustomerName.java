package com.tgcs.tgcp.bridge.adapter;

public class CustomerName {

    private String firstName;

    private String lastName;


    public String getFirstName() {
        return firstName;
    }

    public CustomerName setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CustomerName setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}
