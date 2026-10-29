package model;

import model.annotation.MaxLength;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicLong NEXT_ID = new AtomicLong(100);

    private final String customerId;
    @MaxLength(50)
    private final String name;
    private final String mobile;
    private final String email;

    public Customer(String name, String mobile, String email) {
        customerId = "CUST" + NEXT_ID.incrementAndGet();
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer{customerId='" + customerId + "', name='" + name + "'}";
    }
}
