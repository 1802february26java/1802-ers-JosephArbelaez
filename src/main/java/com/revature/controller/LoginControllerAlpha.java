package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.service.EmployeeServiceAlpha;

public class LoginControllerAlpha implements LoginController {

	private static LoginController loginController = new LoginControllerAlpha();
	private LoginControllerAlpha() {}
	
	public static LoginController getInstance() {
		return loginController;
	}
	private static Logger logger = Logger.getLogger(EmployeeServiceAlpha.class);
	@Override
	public Object login(HttpServletRequest request) {
		logger.trace("LoginControllerAlpha.login");
		if(request.getMethod().equals("GET")) {
			logger.trace("Getting login view");
			return "login.html";
		}
		System.out.println(request.getParameter("username"));
		System.out.println(request.getParameter("password"));

		// Post
		Employee loggedEmployee = new Employee();
		loggedEmployee.setUsername(request.getParameter("username"));
		loggedEmployee.setPassword(request.getParameter("password"));
		loggedEmployee = EmployeeServiceAlpha.getInstance().authenticate(loggedEmployee);
		
		if (loggedEmployee.getId() == 0) {
			return new ClientMessage("AUTHENTICATION FAILED");
		}
			request.getSession().setAttribute("loggedEmployee", loggedEmployee);
			return loggedEmployee;
	}

	@Override
	public String logout(HttpServletRequest request) {
		request.getSession().setAttribute("loggedCustomer", null);
		return "login.html";
	}

}
