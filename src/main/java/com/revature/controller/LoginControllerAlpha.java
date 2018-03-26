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
			return "login.html";
		}
		System.out.println(request.getParameter("username"));
		System.out.println(request.getParameter("password"));

		// Post
		Employee emp = new Employee();
		emp.setUsername(request.getParameter("username"));
		emp.setPassword(request.getParameter("password"));
		emp = EmployeeServiceAlpha.getInstance().authenticate(emp);
		
		if (emp.getId() == 0) {
			return new ClientMessage("AUTHENTICATION FAILED");
		}
			request.getSession().setAttribute("employee", emp);
			return emp;
	}

	@Override
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "login.html";
	}

}
