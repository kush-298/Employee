package com.paypal.bfs.test.employeeserv.dao;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findEmployeeById(@Param("id") Integer id);

    Integer deleteEmployeeById(@Param("id") Integer id);

}
