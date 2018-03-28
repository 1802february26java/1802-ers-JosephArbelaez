package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.service.EmployeeServiceAlpha;

public class HomeControllerAlpha implements HomeController{

	private static HomeController homeController = new HomeControllerAlpha();
	public HomeControllerAlpha() {}

	public static HomeController getInstance() {
		return homeController;
	}
	private static Logger logger = Logger.getLogger(HomeControllerAlpha.class);

	@Override
	public String showEmployeeHome(HttpServletRequest request) {
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		if(loggedEmployee == null) {
			return "login.html";
		}

		if(loggedEmployee.getEmployeeRole().getId() == 1){
			return "home.html";
		}
		return "managerHome.html";

	}
}
