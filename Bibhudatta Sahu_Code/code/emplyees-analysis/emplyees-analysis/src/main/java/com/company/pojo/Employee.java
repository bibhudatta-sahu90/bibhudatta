package com.company.pojo;

/**
 * Employee Pojo
 * @author Bibhudatta Sahu (bibhudatta.sahu90@gmail.com)
 */
public class Employee {
    public String id;
    public String firstName;
    public String lastName;
    public double salary;
    public String managerId;

    public Employee(String id, String firstName, String lastName, double salary, String managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    /**
     * Get employee full name.
     * @return name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}

