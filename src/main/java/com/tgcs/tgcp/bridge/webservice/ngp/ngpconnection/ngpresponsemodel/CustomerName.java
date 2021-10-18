package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

public class CustomerName {

    private String firstName;
    private String lastName;

    public CustomerName() {
    }

    public CustomerName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

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
