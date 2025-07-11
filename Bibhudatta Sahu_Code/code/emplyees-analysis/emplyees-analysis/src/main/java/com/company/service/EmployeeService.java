package com.company.service;

import com.company.pojo.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines employee related all services.
 * @author Bibhudatta Sahu (bibhudatta.sahu90@gmail.com)
 */
public class EmployeeService {

    private final Map<String, Employee> employeeMap = new HashMap<>();
    private final Map<String, List<Employee>> managerToEmployees = new HashMap<>();

    /**
     * Load employee details from file
     * @param filePath String
     * @throws IOException Exception if file not found
     */
    public void loadEmployees(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];
                String firstName = parts[1];
                String lastName = parts[2];
                double salary = Double.parseDouble(parts[3]);
                String managerId = parts.length > 4 ? parts[4].trim() : "";

                Employee employee = new Employee(id, firstName, lastName, salary, managerId.isEmpty() ? null : managerId);
                employeeMap.put(id, employee);

                if (employee.managerId != null) {
                    if (managerToEmployees.get(employee.managerId) == null) {
                        List<Employee> employees = new ArrayList<>();
                        employees.add(employee);
                        managerToEmployees.put(employee.managerId, employees);
                    } else {
                        managerToEmployees.get(employee.managerId).add(employee);
                    }
                }
            }
        }
    }

    /**
     * Identify employees who earns too little, too much.
     */
    public void analyzeSalaries() {
        for (String managerId : managerToEmployees.keySet()) {
            List<Employee> subordinates = managerToEmployees.get(managerId);
            double avg = subordinates.stream().mapToDouble(e -> e.salary).average().orElse(0.0);
            Employee manager = employeeMap.get(managerId);

            double lowerBound = avg * 1.2;
            double upperBound = avg * 1.5;

            if (manager.salary < lowerBound) {
                System.out.printf("Manager %s earns too little: %.2f below minimum%n",
                        manager.getFullName(), lowerBound - manager.salary);
            } else if (manager.salary > upperBound) {
                System.out.printf("Manager %s earns too much: %.2f above maximum%n",
                        manager.getFullName(), manager.salary - upperBound);
            }
        }
    }

    /**
     * Identify employee who has too long reporting line
     */
    public void detectDeepHierarchy() {
        for (Employee employee : employeeMap.values()) {
            int depth = getManagerDepth(employee);
            if (depth > 4) {
                System.out.printf("Employee %s has too long reporting line: %d levels%n", employee.getFullName(), depth);
            }
        }
    }

    /**
     * Get hierarchy manager depth of an employee
     * @param employee Employee
     * @return depth int
     */
    private int getManagerDepth(Employee employee) {
        int depth = 0;
        while (employee.managerId != null && employeeMap.containsKey(employee.managerId)) {
            employee = employeeMap.get(employee.managerId);
            depth++;
        }
        return depth;
    }
}

