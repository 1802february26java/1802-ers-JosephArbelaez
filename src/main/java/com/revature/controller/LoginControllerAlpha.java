package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.service.EmployeeServiceAlpha;

public class LoginControllerAlpha implements LoginController {

	private static LoginController loginController = new LoginControllerAlpha();
	private LoginControllerAlpha() {}
	
	public static LoginController getInstance() {
		return loginController;
	}
	@Override
	public Object login(HttpServletRequest request) {
		if(request.getMethod().equals("GET")) {
			return "login.html";
		}
		
		// Post
		Employee loggedEmployee = EmployeeServiceAlpha.getInstance().authenticate( new Employee(0, 
																								null, 
																								null, 
																								request.getParameter("username"), 
																								request.getParameter("password"), 
																								null));
		
		if (loggedEmployee.getId() == 0) {
			return new ClientMessage("AUTHENTICATION FAILED");
		} else{
			request.getSession().setAttribute("loggedEmployee", loggedEmployee);
			return loggedEmployee;
		}
	}

	@Override
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "login.html";
	}

}
