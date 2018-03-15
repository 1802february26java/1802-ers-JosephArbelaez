package com.revature.model;

import java.io.Serializable;

/**
 * It defines the type of the Reimbursement request.
 * 
 * @author Revature LLC
 */
public class ReimbursementType implements Serializable {
	
	private static final long serialVersionUID = -6682033542018508191L;

	private int id;		// Primary Key
	private String type;	// Not Null
	
	public ReimbursementType() {}

	public ReimbursementType(int id, String type) {
		this.id = id;
		this.type = type;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ReimbursementType [id=" + id + ", type=" + type + "]";
	}
}
