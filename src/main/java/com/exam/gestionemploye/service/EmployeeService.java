package com.exam.gestionemploye.service;

import com.exam.gestionemploye.model.Employee;
import com.exam.gestionemploye.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final MessageSource messageSource;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, MessageSource messageSource) {
        this.employeeRepository = employeeRepository;
        this.messageSource = messageSource;
    }

    public Employee addEmployee(Employee employee, Locale locale) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (existingEmployee.isPresent()) {
            String errorMessage = messageSource.getMessage("error.email.already.in.use", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }
        employeeRepository.save(employee);
        return employee;
    }

    public Employee getEmployee(Long id, Locale locale) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("error.employee.not.found", null, locale)));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee updateEmployee(Long id, Employee employeeDetails, Locale locale) {
        Employee existingEmployee = getEmployee(id, locale);
        existingEmployee.setFirstName(employeeDetails.getFirstName());
        existingEmployee.setLastName(employeeDetails.getLastName());
        existingEmployee.setEmail(employeeDetails.getEmail());
        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id, Locale locale) {
        Employee employee = getEmployee(id, locale);
        employeeRepository.delete(employee);
    }
}
