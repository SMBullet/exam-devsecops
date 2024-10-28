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
        // Default to English if locale is null
        Locale effectiveLocale = (locale != null) ? locale : Locale.ENGLISH;

        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (existingEmployee.isPresent()) {
            String errorMessage = messageSource.getMessage("email.already.in.use", null, effectiveLocale);
            throw new IllegalArgumentException(errorMessage);
        }

        employeeRepository.save(employee);
        String successMessage = messageSource.getMessage("employee.added", null, effectiveLocale);
        System.out.println(successMessage);
        return employee;
    }

    public Employee getEmployee(Long id, Locale locale) {
        Locale effectiveLocale = (locale != null) ? locale : Locale.ENGLISH;

        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("employee.not.found", null, effectiveLocale)));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee updateEmployee(Long id, Employee employeeDetails, Locale locale) {
        Locale effectiveLocale = (locale != null) ? locale : Locale.ENGLISH;

        Employee existingEmployee = getEmployee(id, effectiveLocale);
        existingEmployee.setFirstName(employeeDetails.getFirstName());
        existingEmployee.setLastName(employeeDetails.getLastName());
        existingEmployee.setEmail(employeeDetails.getEmail());

        String successMessage = messageSource.getMessage("employee.added", null, effectiveLocale); // This should be for updating
        System.out.println(successMessage);

        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id, Locale locale) {
        Locale effectiveLocale = (locale != null) ? locale : Locale.ENGLISH;

        Employee employee = getEmployee(id, effectiveLocale);
        employeeRepository.delete(employee);

    }
}
