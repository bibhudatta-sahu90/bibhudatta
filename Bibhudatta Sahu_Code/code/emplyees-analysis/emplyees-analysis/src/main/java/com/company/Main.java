package com.company;

import com.company.service.EmployeeService;
import java.io.IOException;

/**
 * Entry point of program to start execution
 * @author Bibhudatta Sahu (bibhudatta.sahu90@gmail.com)
 */
public class Main {
    public static void main(String[] args) {
        EmployeeService service = new EmployeeService();
        try {
            //csv file path
            service.loadEmployees("src/main/resources/employees.csv");
            System.out.println("Salary Analysis:");
            service.analyzeSalaries();
            System.out.println("\nReporting Line Analysis:");
            service.detectDeepHierarchy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}