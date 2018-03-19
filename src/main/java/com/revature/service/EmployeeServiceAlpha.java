package com.revature.service;

import java.sql.SQLException;
import java.util.Set;
import org.apache.log4j.Logger;

import com.revature.exception.InputException;
import com.revature.model.Employee;
import com.revature.model.EmployeeToken;
import com.revature.repository.EmployeeRepositoryjbdc;

public class EmployeeServiceAlpha implements EmployeeService {

	private static EmployeeServiceAlpha service = new EmployeeServiceAlpha();
	private static Logger logger = Logger.getLogger(EmployeeServiceAlpha.class);
	private static EmployeeRepositoryjbdc repository = EmployeeRepositoryjbdc.getInstance();

	private EmployeeServiceAlpha(){};
	public static EmployeeServiceAlpha getInstance(){
		return service;
	}

	@Override
	public Employee authenticate(Employee employee) {
		Employee selectedEmployee = repository.select(employee.getUsername());
		if (selectedEmployee.getPassword().equals(repository.getPasswordHash(employee))) {
			logger.info("Employee authenticated.");
			return selectedEmployee;
		}
		logger.info("Employee authentication failed. EmployeeServiceAlpha.authenticate.");
		return null;
	}

	@Override
	public Employee getEmployeeInformation(Employee employee) {
		if (employee.getId() != 0){
			int employeeID = employee.getId();
			Employee emp = repository.select(employeeID);
			logger.info("Employee " + employeeID + " acquired.");
			return emp;
		}
		logger.error("Error acquiring employee information. EmployeeServiceAlpha.getEmployeeInformation.");
		return null;
	}

	@Override
	public Set<Employee> getAllEmployeesInformation() {
		Set<Employee> allEmployees = repository.selectAll();
		if (allEmployees.size() > 0){
			logger.info("All employees Acquired.");
			return allEmployees;
		}
		logger.error("Error acquiring all Employees Information. EmployeeServiceAlpha.getAllEmployeesInformation.");
		return null;
	}

	@Override
	public boolean createEmployee(Employee employee) {
		try { 
			repository.insert(employee);
			return true;
		} catch (SQLException e) {
			if (employee.getFirstName() == null){
				logger.error("You must enter a first name.");
			}
			if (employee.getLastName() == null){
				logger.error("You must enter a last name.");

			}
			if (employee.getUsername() == null){
				logger.error("You must enter a username.");

			}
			if (employee.getPassword() == null){
				logger.error("You must enter a password.");
			} 
		} 
		
		return false;
	}

	@Override
	public boolean updateEmployeeInformation(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updatePassword(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUsernameTaken(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPasswordToken(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deletePasswordToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTokenExpired(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

}
