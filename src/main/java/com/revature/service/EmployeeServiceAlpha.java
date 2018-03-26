package com.revature.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
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
		Employee loggedEmployee = repository.getInstance().select(employee.getUsername());
		 if(loggedEmployee.getPassword().equals(EmployeeRepositoryjbdc.getInstance().getPasswordHash(employee))) {
			logger.info("Employee authenticated.");
			//System.out.println(loggedEmployee);
			return loggedEmployee;
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
		if (repository.update(employee)){
			logger.info("Employee Updated!");
			return true;
		} else {
			logger.error("Employee update failed. =(");
			return false;
		}
		
	}

	@Override
	public boolean updatePassword(Employee employee) {
		if (repository.update(employee)){
			logger.info("Employee Updated!");
			return true;
		} else {
			logger.error("Employee update failed. =(");
			return false;
		}
	}

	@Override
	public boolean isUsernameTaken(Employee employee) {
		
		Employee emp1 = repository.select(employee.getUsername());
		Employee empNull = new Employee(0, null, null, null, null, null);
		if ( emp1.equals(empNull)) {
			logger.info("Username has not been taken");
			return true;
		}
		logger.info("Username already exists within the database.");
		return false;
	}

	@Override
	public boolean createPasswordToken(Employee employee) {
		LocalDateTime now = LocalDateTime.now();
		String hash = now.toString();
		hash = hash + employee.getUsername();
		hash = Integer.toString(hash.hashCode());

		if (employee.getId() > 0) {
			// If there is an employeeID, create a new PasswordToken
			EmployeeToken et = new EmployeeToken(0, hash, now, employee);
			repository.insertEmployeeToken(et);
			return true;
		}
		return false;
	}

	@Override
	public boolean deletePasswordToken(EmployeeToken employeeToken) {
		try {
			repository.deleteEmployeeToken(employeeToken);
		} catch (SQLException e) {
			logger.info("Token does not exist within the database.");
		}
		return false;
	}

	@Override
	public boolean isTokenExpired(EmployeeToken employeeToken) {

		EmployeeToken et = repository.selectEmployeeToken(employeeToken);
		if (et == null){
			logger.trace("Token does not exist within the database.");
		}

		LocalDateTime tokenDate = et.getCreationDate();
		LocalDateTime now = LocalDateTime.now();

		// I only want the token to last for seven days. If it's more it gets deleted
		// if it's less than 7 days it gets to be used.
		long days = ChronoUnit.DAYS.between(tokenDate, now);
		if (days >= 7){
			try {
				repository.deleteEmployeeToken(employeeToken);
			} catch (SQLException e) {
				logger.error("Error deleting token.");
			}
			return true;
		}
		return false;
	}
	public static void main(String[] args) {
		Employee emp = new Employee(41, "anthony", "pena", "dschultz", "1", "penaa@gmail.com",new EmployeeRole(2,"MANAGER"));
		Employee emp2 = new Employee(61, "david", "spektz", "dspektz", "2", "dspektz@gmail.com",new EmployeeRole(2,"MANAGER"));
		Employee emp3 = new Employee(0, "walker", "lavadera", "dungeonmaster", "1", "walker@gmail.com",new EmployeeRole(2,"MANAGER"));
		EmployeeToken et = new EmployeeToken(22, null, null, emp2);
		EmployeeServiceAlpha esa = new EmployeeServiceAlpha();
		
		// Authenticate Test
		//esa.getInstance().authenticate(emp);
		
		// getEmployeeInformation TEST
		//System.out.println(esa.getInstance().getEmployeeInformation(emp));
		
		// getAllEmployeeInformation
		//System.out.println(esa.getInstance().getAllEmployeesInformation());
		//System.out.println(esa.getInstance().getAllEmployeesInformation().size());
		
		//create Employee
		//esa.getInstance().createEmployee(emp2);
		
		//update Employee
		//System.out.println(esa.getInstance().updateEmployeeInformation(emp2));
		
		// Update Password
		//System.out.println(esa.getInstance().updatePassword(emp));
		
		// Is username taken?
		//System.out.println(esa.getInstance().isUsernameTaken(emp3));
		
		// Create Password Token
		//System.out.println(esa.getInstance().createPasswordToken(emp2));
		
		// Delete Password Token
		//System.out.println(esa.getInstance().deletePasswordToken(et));
		
		
		// Is token expired?
		//System.out.println(esa.getInstance().isTokenExpired(et));
	}
}
