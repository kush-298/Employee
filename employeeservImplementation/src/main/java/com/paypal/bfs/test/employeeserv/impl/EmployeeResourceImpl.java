package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.dao.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.exception.ActionNotAllowedException;
import com.paypal.bfs.test.employeeserv.exception.EntityNotFoundException;
import com.paypal.bfs.test.employeeserv.exception.InternalServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;

/**
 * Implementation class for employee resource.
 */
@RestController
@Transactional
public class EmployeeResourceImpl implements EmployeeResource {

    private static final Logger logger = LogManager.getLogger(EmployeeResourceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ResponseEntity<Employee> employeeGetById(Integer id) throws Exception {
        logger.debug("Fetching employee details for emp id {}", id);
        Employee employee;
        try {
            employee = employeeRepository.findEmployeeById(id);
            if (employee == null) {
                throw new EntityNotFoundException(String.format("Employee details not found for employee id %s", id));
            }
        } catch (EntityNotFoundException ex1) {
            throw ex1;
        } catch (Exception ex) {
            throw new InternalServerException("Something went wrong while fetching employee details", ex);
        }
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @Override
    public ResponseEntity createEmployee(Employee employee) throws Exception {
        logger.debug("Inserting employee details");
        Map<String, Object> response = new HashMap<>();
        try {
            this.validateEmployeeDetails(employee);
            employeeRepository.save(employee);
            response.put("message", "Created Successfully");
            response.put("Employee Id", employee.getId());
        } catch (ActionNotAllowedException ex1) {
            throw ex1;
        } catch (Exception ex) {
            throw new InternalServerException("Something went wrong while saving employee details", ex);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity updateEmployee(Integer id, Employee employee) throws Exception {
        logger.debug("Updating employee details for emp id {}", id);
        Employee persistedEmployee = employeeRepository.findEmployeeById(id);
        if (persistedEmployee == null) {
            throw new EntityNotFoundException(String.format("Employee details not found for employee id %s", id));
        }
        Map<String, Object> response = new HashMap<>();
        try {
            employee.setId(persistedEmployee.getId());
            this.validateEmployeeDetails(employee);
            employeeRepository.save(employee);
            response.put("message", "Updated Successfully");
            response.put("Employee Id", employee.getId());
        } catch (ActionNotAllowedException ex1) {
            throw ex1;
        } catch (Exception ex) {
            throw new InternalServerException("Something went wrong while updating employee details", ex);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteEmployee(Integer id) throws Exception {
        logger.debug("Deleting employee details for emp id {}", id);
        Employee persistedEmployee = employeeRepository.findEmployeeById(id);
        if (persistedEmployee == null) {
            throw new EntityNotFoundException(String.format("Employee details not found for employee id %s", id));
        }
        Map<String, Object> response = new HashMap<>();
        try {
            employeeRepository.deleteEmployeeById(id);
            response.put("message", "Deletion successful");
        } catch (Exception ex) {
            throw new InternalServerException("Something went wrong while saving employee details", ex);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity fetchAllEmployees() throws Exception {
        logger.debug("Fetching list of employee details");
        Map<String, Object> response = new HashMap<>();
        List<Employee> employees;
        try {
            employees = employeeRepository.findAll();
            response.put("data", employees);
            response.put("totalCount", employees.size());
        } catch (Exception ex) {
            throw new InternalServerException("Something went wrong while saving employee details", ex);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void validateEmployeeDetails(Employee employee) throws ActionNotAllowedException {
        if (StringUtils.isEmpty(employee.getFirstName()) || employee.getFirstName().length() > 255) {
            throw new ActionNotAllowedException("Length of first name must be between 1 to 255");
        } else if (StringUtils.isEmpty(employee.getLastName()) || employee.getLastName().length() > 255) {
            throw new ActionNotAllowedException("Length of last name must be between 1 to 255");
        } else if (employee.getAddresses() != null || !employee.getAddresses().isEmpty()) {
            this.validateAddressDetails(employee.getAddresses());
        }
    }

    private void validateAddressDetails(List<Address> addresses) throws ActionNotAllowedException {
        for (Address address : addresses) {
            if (StringUtils.isEmpty(address.getLine1()) || address.getLine1().length() > 1024) {
                throw new ActionNotAllowedException("Length of address line 1 must be between 1 to 1024");
            } else if (address.getLine2() != null && address.getLine2().length() > 1024) {
                throw new ActionNotAllowedException("Length of address line 2 can not be greater than 1024");
            } else if (StringUtils.isEmpty(address.getCity())) {
                throw new ActionNotAllowedException("City can not be empty");
            } else if (StringUtils.isEmpty(address.getState())) {
                throw new ActionNotAllowedException("State can not be empty");
            } else if (StringUtils.isEmpty(address.getCountry())) {
                throw new ActionNotAllowedException("Country can not be empty");
            } else if (StringUtils.isEmpty(address.getZipCode())) {
                throw new ActionNotAllowedException("Zip code can not be empty");
            }
        }
    }
}
