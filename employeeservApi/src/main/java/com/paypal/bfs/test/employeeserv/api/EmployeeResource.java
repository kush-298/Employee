package com.paypal.bfs.test.employeeserv.api;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

    /**
     * Retrieves the {@link Employee} resource by id.
     *
     * @param id employee id.
     * @return {@link Employee} resource.
     */
    @GetMapping("/v1/bfs/employee/{id}")
    ResponseEntity<Employee> employeeGetById(@PathVariable("id") Integer id) throws Exception;

    @PostMapping("/v1/bfs/employee")
    ResponseEntity createEmployee(@RequestBody Employee employee) throws Exception;

    @PutMapping("/v1/bfs/employee/{id}")
    ResponseEntity updateEmployee(@PathVariable("id") Integer id, @RequestBody Employee employee) throws Exception;

    @DeleteMapping("/v1/bfs/employee/{id}")
    ResponseEntity deleteEmployee(@PathVariable("id") Integer id) throws Exception;

    @GetMapping("/v1/bfs/employees")
    ResponseEntity fetchAllEmployees() throws Exception;

}
