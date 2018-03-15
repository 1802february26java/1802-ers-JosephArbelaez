package com.revature.model;

import java.io.Serializable;

/**
 * It defines whether an Employee is a regular employee or a manager.
 * 
 * @author Revature LLC
 */
public class EmployeeRole implements Serializable {

	private static final long serialVersionUID = -8261405093505107417L;

	private int id;			// Primary Key
	private String type;	// Not null
	
	public EmployeeRole() {}
	
	public EmployeeRole(String type) {
		this.type = type;
	}
	
	public EmployeeRole(int id, String type) {
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
		return "EmployeeRole [id=" + id + ", type=" + type + "]";
	}
}
