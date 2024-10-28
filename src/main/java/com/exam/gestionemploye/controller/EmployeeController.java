package com.exam.gestionemploye.controller;

import com.exam.gestionemploye.model.Employee;
import com.exam.gestionemploye.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final MessageSource messageSource;

    @Autowired
    public EmployeeController(EmployeeService employeeService, MessageSource messageSource) {
        this.employeeService = employeeService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            employeeService.addEmployee(employee, locale);
            return new ResponseEntity<>(messageSource.getMessage("success.employee.added", null, locale), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            Employee employee = employeeService.getEmployee(id, locale);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails, locale);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            employeeService.deleteEmployee(id, locale);
            return new ResponseEntity<>(messageSource.getMessage("success.employee.deleted", null, locale), HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
