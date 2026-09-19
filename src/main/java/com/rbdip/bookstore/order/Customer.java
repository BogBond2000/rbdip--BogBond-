package com.rbdip.bookstore.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String address;

    private String phone;

    protected Customer() {
        // for JPA
    }

    public Customer(String fullName, String address, String phone) {
        NameParts parts = splitFullName(fullName);
        this.firstName = parts.firstName();
        this.lastName = parts.lastName();
        this.address = address;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        if (lastName == null || lastName.isBlank()) {
            return firstName;
        }
        return firstName + " " + lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    private static NameParts splitFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return new NameParts("", "");
        }
        int spaceIndex = fullName.indexOf(' ');
        if (spaceIndex < 0) {
            return new NameParts(fullName.trim(), "");
        }
        return new NameParts(
                fullName.substring(0, spaceIndex).trim(),
                fullName.substring(spaceIndex + 1).trim());
    }

    private record NameParts(String firstName, String lastName) {
    }
}
