package com.revature.ajax;

public class ClientMessage {

	private String Message;
	public ClientMessage(){}
	public ClientMessage(String message) {
		this.Message = message;
	}
	
	public String getMessage() {
		return Message;
	}
	
	public void setMessage(String message) {
		Message = message;
	}
	
	@Override
	public String toString() {
		return "ClientMessage [Message=" + Message + "]";
	}
}
