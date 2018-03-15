package com.revature.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Used for password recovery. Once a token is successfully used it should be deleted.
 * 
 * You can go ahead and try to provide a timer logic for them to expire.
 * 
 * @author Revature LLC
 */
public class EmployeeToken implements Serializable {

	private static final long serialVersionUID = -6347088033833089677L;

	private int id; 		// Primary Key
	private String token; 	// Not Null, recommended to use hashing functions combining the timestamp of it was created
	private LocalDateTime creationDate; // Not null, represents TIMESTAMP of when it was created.
	private Employee requester; // Not Null, FK coming from Employee which represents the requester.

	public EmployeeToken() {}
	
	public EmployeeToken(int id, String token, LocalDateTime creationDate, Employee requester) {
		this.id = id;
		this.token = token;
		this.creationDate = creationDate;
		this.requester = requester;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Employee getRequester() {
		return requester;
	}

	public void setRequester(Employee requester) {
		this.requester = requester;
	}

	@Override
	public String toString() {
		return "EmployeeToken [id=" + id + ", token=" + token + ", creationDate=" + creationDate + ", requester="
				+ requester + "]";
	}
}
