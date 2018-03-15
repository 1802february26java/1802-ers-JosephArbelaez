package com.revature.model;

import java.io.Serializable;

/**
 * It defines the current status of a Reimbursement request.
 * 
 * @author Revature LLC
 */
public class ReimbursementStatus implements Serializable {

	private static final long serialVersionUID = 7006265099284488241L;

	private int id; // PK
	private String status; // Not NULL, (PENDING, APPROVED, DECLINED)

	public ReimbursementStatus() {}
	
	public ReimbursementStatus(int id, String status) {
		this.id = id;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ReimbursementStatus [id=" + id + ", status=" + status + "]";
	}
}
