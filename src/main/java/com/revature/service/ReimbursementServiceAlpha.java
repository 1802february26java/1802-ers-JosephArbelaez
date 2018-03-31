package com.revature.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
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
		Reimbursement reimbursementNull = new Reimbursement(1, null, null, 0, null, null, null, null, null);
		if (reimbursement.equals(reimbursementNull)) {
			return false;
		}
		return repository.insert(reimbursement);
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
			try{
				Reimbursement r = repository.select(reimbursement.getId());
				logger.info("Select completed!");
				return r;
			} catch (SQLException e) {
				logger.error("Issue selecting a single request ReimbursementServiceAlpha.getSingleRequest");
			}
			return null;
	}

	@Override
	public Set<Reimbursement> getUserPendingRequests(Employee employee) {
		if (employee.getId() != 0){
			Set<Reimbursement> set = new HashSet<Reimbursement>();
			set = repository.selectPending(employee.getId());
			if (set.size() > 0){	
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
			if (set.size() > 0){	
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
		Set<Reimbursement> set = repository.selectAllPending();
		if (set.size() > 0){	
			logger.info("Successfully gathered all pending requests.");
			return set;
		} 
		logger.error("Issue selecting all pending requests.  ReimbursementServiceAlpha.getAllPendingRequests.");
		return null;
	}

	@Override
	public Set<Reimbursement> getAllResolvedRequests() {
		Set<Reimbursement> set = new HashSet<Reimbursement>();
		set = repository.selectAllFinalized();
		if (set.size() > 0){	
			logger.info("Successfully gathered all resolved requests.");
			return set;
		} 
		logger.error("Issue selecting all finalized requests.  ReimbursementServiceAlpha.getAllResolvedRequests.");
		return null;
	}

	@Override
	public Set<ReimbursementType> getReimbursementTypes() {
		logger.trace("ReimbursementServiceAlpha.getReimbursementTypes.");
		Set<ReimbursementType> set = new HashSet<ReimbursementType>();
		set = repository.selectTypes();
		if (set.size() > 0){	
			logger.info("Successfully gathered all Types.");
			return set;
		} 
		logger.error("Issue selecting all ReimbursementTypes.  ReimbursementServiceAlpha.getAllTypes");
		return null;
	}
	
	public static void main(String[] args) {
		ReimbursementServiceAlpha rsa = new ReimbursementServiceAlpha();
		Employee joe = new Employee(21,
				"joseph",
				"arbelaez", 
				"a", 
				"1", 
				"jarbelaez@gmail.com",
				new EmployeeRole(2,
						"MANAGER"));
		Employee danielle = new Employee(43,
				"danielle",
				"schultz", 
				"dschultz", 
				"1", 
				"dschultz@gmail.com",
				new EmployeeRole(2,
						"MANAGER"));
		ReimbursementStatus rs = new ReimbursementStatus(1,
				"PENDING");
		ReimbursementStatus rs2 = new ReimbursementStatus(3,
				"APPROVED");
		ReimbursementStatus rs3 = new ReimbursementStatus(2,
				"DECLINED");
		ReimbursementType rt = new ReimbursementType(2,
				"COURSE");
		ReimbursementType rt2 = new ReimbursementType(3,
				"CERTIFICATION");
		Reimbursement r = new Reimbursement(1,
				LocalDateTime.now(), 
				null, 
				10.00, 
				"Enthuware", 
				joe, 
				null, 
				rs3, 
				rt);
		Reimbursement r2 = new Reimbursement(1,
				LocalDateTime.now(), 
				LocalDateTime.now(), 
				10.00, 
				"Enthuware", 
				joe, 
				danielle, 
				rs2, 
				rt2);
		
		// Submit Request Test
		//System.out.println(rsa.getInstance().submitRequest(r));
		
		// Finalize Request Test
		//System.out.println(rsa.getInstance().finalizeRequest(r2));
		
		// Get Single Request Test
		// System.out.println(rsa.getInstance().getSingleRequest(r2));
		
		// Get User Pending Request
		// System.out.println(rsa.getInstance().getUserPendingRequests(anthony));
		
		// Get user Finalized Requests
		//System.out.println(rsa.getInstance().getUserFinalizedRequests(anthony).size());		
	
		// Get all pending requests
		//System.out.println(rsa.getInstance().getAllPendingRequests().size());
		
		// get all resolved requests
		//System.out.println(rsa.getInstance().getAllResolvedRequests().size());
		
		// Get all reimbursement types
		//System.out.println(rsa.getInstance().getReimbursementTypes().size());
	}

}
