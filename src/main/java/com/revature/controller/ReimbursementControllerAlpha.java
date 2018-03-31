package com.revature.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.service.ReimbursementServiceAlpha;

public class ReimbursementControllerAlpha implements ReimbursementController {

	private static ReimbursementController reimbursementController = new ReimbursementControllerAlpha();

	private ReimbursementControllerAlpha() {}

	public static ReimbursementController getInstance(){
		return reimbursementController;
	}

	private static Logger logger = Logger.getLogger(EmployeeInformationControllerAlpha.class);

	@Override
	public Object submitRequest(HttpServletRequest request) {

		logger.trace("ReimbursementControllerAlpha.submitRequest");

		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		if(loggedEmployee == null) {
			return "login.html";
		}

		if (loggedEmployee.getEmployeeRole().getId() == 2){
			return "managerHome.html";
		}

		ReimbursementStatus status = new ReimbursementStatus(1,"PENDING");
		ReimbursementType type = new ReimbursementType(Integer.parseInt(request.getParameter("reimbursementTypeId")), request.getParameter("reimbursementTypeName"));

		Reimbursement reimbursement = new Reimbursement(0,
				LocalDateTime.now(),
				null,
				Double.parseDouble(request.getParameter("amount")),
				request.getParameter("description"),
				loggedEmployee,
				null,
				status,
				type);
		if (ReimbursementServiceAlpha.getInstance().submitRequest(reimbursement)) {
			return new ClientMessage("REGISTRATION SUCCESSFUL");
		} else {
			return new ClientMessage("SOMETHING WENT WRONG");
		}
	}

	@Override
	public Object singleRequest(HttpServletRequest request) {
		logger.trace("ReimbursementControllerAlpha.submitRequest");

		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		if(loggedEmployee == null) {
			return "login.html";
		}

		Reimbursement reimbursement = new Reimbursement(Integer.parseInt(request.getParameter("reimbursementId")),
				LocalDateTime.now(),
				null,
				10.00,
				null,
				null,
				null,
				new ReimbursementStatus(),
				new ReimbursementType());

		Reimbursement r = ReimbursementServiceAlpha.getInstance().getSingleRequest(reimbursement);
		if (r != reimbursement){
			return new ClientMessage ("Reimbursement Successful");
		}

		return new ClientMessage ("Select Reimbursement Failed");
	}

	@Override
	public Object multipleRequests(HttpServletRequest request) {
		logger.trace("ReimbursementControllerAlpha.multipleRequests");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");

		if(loggedEmployee == null) {
			return "login.html";
		}
		
		if(loggedEmployee.getEmployeeRole().getId() == 1) {
			logger.trace("Employee Identified as Employee");
			if(request.getParameter("fetch").equals("finalized")){
				logger.trace("ReimbursementControllerAlpha.multipleRequests - Finalized Employee Route.");
				Set<Reimbursement> reimbursements = new HashSet<Reimbursement>(ReimbursementServiceAlpha.getInstance().getUserFinalizedRequests(loggedEmployee));
				return reimbursements;
			}
			if(request.getParameter("fetch").equals("pending")){
				logger.trace("ReimbursementControllerAlpha.multipleRequests - Pending Employee Route.");
				Set<Reimbursement> reimbursements = new HashSet<Reimbursement>(ReimbursementServiceAlpha.getInstance().getUserPendingRequests(loggedEmployee));
				logger.trace(reimbursements);
				return reimbursements;
			}
		}
		
		if (loggedEmployee.getEmployeeRole().getId() == 2){
			
		}

		return new ClientMessage("Must add Manager code to ReimbursementControllerAlpha.multipleRequests.");
	}

	@Override
	public Object finalizeRequest(HttpServletRequest request) {
		logger.trace("ReimbursementControllerAlpha.finalizeRequest");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if(loggedEmployee == null) {
			logger.trace("Not signed in");
			return "login.html";
		}
		
		Reimbursement reimbursement = new Reimbursement(Integer.parseInt(request.getParameter("reimbursementId")));
		ReimbursementStatus status = new ReimbursementStatus(Integer.parseInt(request.getParameter("statusId")),request.getParameter("status"));
		Reimbursement update = ReimbursementServiceAlpha.getInstance().getSingleRequest(reimbursement);
		update.setStatus(status);
		update.setResolved(LocalDateTime.now());
		
		if(ReimbursementServiceAlpha.getInstance().finalizeRequest(update)){
			return new ClientMessage("UPDATE SUCCESSFUL");
		} else {
			return new ClientMessage("SOMETHING WENT WRONG");
		}
	}
	@Override
	public Object getRequestTypes(HttpServletRequest request) {
		logger.trace("ReimbursementControllerAlpha.getRequestTypes");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if(loggedEmployee == null) {
			logger.trace("Not signed in");
			return "login.html";
		}
		Set<ReimbursementType> types = ReimbursementServiceAlpha.getInstance().getReimbursementTypes();
		return types;
	}

}
