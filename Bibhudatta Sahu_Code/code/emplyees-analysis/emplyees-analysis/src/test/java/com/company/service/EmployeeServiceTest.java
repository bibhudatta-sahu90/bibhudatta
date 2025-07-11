package com.company.service;

import com.company.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class EmployeeServiceTest {

    private EmployeeService service;

    @Before
    public void setUp() throws Exception {
        service = new EmployeeService();
        String csvContent = "Id,firstName,lastName,salary,managerId\n" +
                "123,Joe,Doe,60000,\n" +
                "124,Martin,Chekov,45000,123\n" +
                "125,Bob,Ronstad,47000,123\n" +
                "300,Alice,Hasacat,50000,124\n" +
                "305,Brett,Hardleaf,34000,300\n" +
                "400,Lee,Jack,25000,125\n" +
                "401,Doe,John,15000,400\n" +
                "402,John,Cena,5000,401\n" +
                "403,Cruse,Tom,2000,402\n" +
                "404,Holand,Tom,1000,403";

        //csv file path location
        File testFile = new File("src/test/resources/test_employees.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write(csvContent);
        }
        service.loadEmployees("src/test/resources/test_employees.csv");
    }

    @Test
    public void testEmployeeLoading() throws NoSuchFieldException, IllegalAccessException {
        Field employeeMap
                = EmployeeService.class.getDeclaredField("employeeMap");
        employeeMap.setAccessible(true);
        Map map = (Map) employeeMap.get(service);
        assertEquals(10, map.size());
    }

    @Test
    public void testSalaryAnomalies() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        service.analyzeSalaries();
        String output = out.toString();

        assertTrue(output.contains("Manager Martin Chekov earns too little"));
        assertTrue(output.contains("Manager Cruse Tom earns too much"));
    }

    @Test
    public void testReportingLineDepth() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        service.detectDeepHierarchy();
        String output = out.toString();

        assertTrue(output.contains("Employee Cruse Tom has too long reporting line: 5 levels"));
        assertTrue(output.contains("Employee Holand Tom has too long reporting line: 6 levels"));
    }
}

