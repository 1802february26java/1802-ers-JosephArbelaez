package com.revature.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.service.EmployeeServiceAlpha;

public class EmployeeInformationControllerAlpha implements EmployeeInformationController {

	private static EmployeeInformationController employeeInformationController = new EmployeeInformationControllerAlpha();

	private EmployeeInformationControllerAlpha() {}

	public static EmployeeInformationController getInstance(){
		return employeeInformationController;
	}

	private static Logger logger = Logger.getLogger(EmployeeInformationControllerAlpha.class);

	@Override
	public Object registerEmployee(HttpServletRequest request) {
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		logger.trace(loggedEmployee.getEmployeeRole().getId());

		if (loggedEmployee.getEmployeeRole().getId() == 1){
			logger.trace("Within return to 403.");
			return "403Employee.html";
		}
		
		if (request.getMethod().equals("GET")){
			logger.trace("EmployeeInformationController.registerEmployee GET");

			return "register.html";
		}
		


		logger.trace("EmployeeInformationController.registerEmployee POST");

		Employee employee = new Employee(0, request.getParameter("firstName"),
				request.getParameter("lastName"),
				request.getParameter("username"),
				request.getParameter("password"),
				request.getParameter("email"));

		if (EmployeeServiceAlpha.getInstance().isUsernameTaken(employee)){
			return new ClientMessage("USERNAME IS TAKEN");
		}
		if (EmployeeServiceAlpha.getInstance().createEmployee(employee)) {
			return new ClientMessage("REGISTRATION SUCCESSFUL");
		} else {
			return new ClientMessage("SOMETHING WENT WRONG");
		}
	}

	@Override
	public Object updateEmployee(HttpServletRequest request) {
		if (request.getMethod().equals("GET")){
			return "login.html";
		}
		logger.trace("Inside updateEmployee");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		if (loggedEmployee == null) {
			return "login.html";
		}

		Employee employee = new Employee (
				loggedEmployee.getId(),
				request.getParameter("firstname"),
				request.getParameter("lastname"),
				request.getParameter("username"),
				null,
				request.getParameter("email"),
				loggedEmployee.getEmployeeRole());
		logger.trace(employee);
		if (EmployeeServiceAlpha.getInstance().updateEmployeeInformation(employee)) {
			logger.trace("Successful update! EmployeeInformationController.updateEmployee");
			return new ClientMessage("REGISTRATION SUCCESSFUL");
		} else {
			return new ClientMessage("SOMETHING WENT WRONG");
		}
	}

	@Override
	public Object viewEmployeeInformation(HttpServletRequest request) {
		if (request.getMethod().equals("GET")){
			return "login.html";
		} 

		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if(loggedEmployee == null) {
			return"login.html";
		} else {
			return EmployeeServiceAlpha.getInstance().getEmployeeInformation(loggedEmployee);
		}
	}

	@Override
	public Object viewAllEmployees(HttpServletRequest request) {
		logger.trace("EmployeeInformationController.viewAllEmployees");
		if (request.getMethod().equals("GET")){
			return "login.html";
		}
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("Employee");
		
		logger.trace(loggedEmployee);
		/* If customer is not logged in */
		if(loggedEmployee == null) {
			return "login.html";
		} 
		if (loggedEmployee.getEmployeeRole().getId() == 2) {
			logger.trace("Employee is Manager");
			if (request.getParameter("num").equals("2")){
				logger.trace("Num = 2");
				Set<Employee> set = EmployeeServiceAlpha.getInstance().getAllEmployeesInformation(2);
				logger.trace(set);
				return set;
			}
			if (request.getParameter("num").equals("3")){
				Set<Employee> set = EmployeeServiceAlpha.getInstance().getAllEmployeesInformation(3);
				logger.trace(set);
				return set;
			}	
		}
		return "404.html";
	}

	@Override
	public Object usernameExists(HttpServletRequest request) {
		if (request.getMethod().equals("GET")){
			return "register.html";
		}

		boolean exists = EmployeeServiceAlpha.getInstance().isUsernameTaken(new Employee(0, request.getParameter("username"), null, null, null, null));

		if(exists) {
			return new ClientMessage("Username has already been taken");
		} else {
			return new ClientMessage("Username Available");
		}

	}
}