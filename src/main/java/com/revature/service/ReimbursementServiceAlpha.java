package com.revature.service;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementType;
import com.revature.repository.EmployeeRepositoryjbdc;
import com.revature.repository.ReimbursementRepositoryJDBC;

public class ReimbursementServiceAlpha implements ReimbursementService {

	private static ReimbursementServiceAlpha service = new ReimbursementServiceAlpha();
	private static Logger logger = Logger.getLogger(ReimbursementServiceAlpha.class);
	private static ReimbursementRepositoryJDBC repository = ReimbursementRepositoryJDBC.getInstance();

	private ReimbursementServiceAlpha(){};
	public static ReimbursementServiceAlpha getInstance(){
		return service;
	}

	@Override
	public boolean submitRequest(Reimbursement reimbursement) {
		if (reimbursement.getRequested() == null || reimbursement.getAmount() == 0 || reimbursement.getRequester() == null || reimbursement.getStatus() == null|| reimbursement.getType() == null) {
			repository.insert(reimbursement);
			return true;
		}
		return false;
	}

	@Override
	public boolean finalizeRequest(Reimbursement reimbursement) {
		try {
			repository.update(reimbursement);
			return true;
		} catch (SQLException e) {
			logger.error("Unable to update reimbursement.");
		}
		return false;
	}

	@Override
	public Reimbursement getSingleRequest(Reimbursement reimbursement) {
		if (reimbursement.getId() != 0){
			try{
				repository.select(reimbursement.getId());
				logger.info("Select completed!");
			} catch (SQLException e) {
				logger.error("Issue selecting a single request ReimbursementServiceAlpha.getSingleRequest");
			}
		}
		return null;
	}

	@Override
	public Set<Reimbursement> getUserPendingRequests(Employee employee) {
		if (employee.getId() != 0){
			Set<Reimbursement> set = new HashSet<Reimbursement>();
			set = repository.selectPending(employee.getId());
			if (set.size() < 1){	
				logger.info("Successfully gathered User pending requests.");
				return set;
			} 
			logger.error("Issue selecting a user's pending requests.  ReimbursementServiceAlpha.getUserPendingRequests.");
			return null;
		}
		logger.error("Employee must have an ID.");
		return null;
	}

	@Override
	public Set<Reimbursement> getUserFinalizedRequests(Employee employee) {
		if (employee.getId() != 0){
			Set<Reimbursement> set = new HashSet<Reimbursement>();
			set = repository.selectFinalized(employee.getId());
			if (set.size() < 1){	
				logger.info("Successfully gathered User finalized requests.");
				return set;
			} 
			logger.error("Issue selecting a user's finalized requests.  ReimbursementServiceAlpha.getUserFinalizedRequests.");
			return null;
		}
		logger.error("Employee must have an ID.");
		return null;
	}

	@Override
	public Set<Reimbursement> getAllPendingRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> getAllResolvedRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ReimbursementType> getReimbursementTypes() {
		// TODO Auto-generated method stub
		return null;
	}

}
