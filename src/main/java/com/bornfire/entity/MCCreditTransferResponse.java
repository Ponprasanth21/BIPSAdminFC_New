package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class MCCreditTransferResponse {
	private String TranID;
	private String TranDateTime;
	private String Status;
	
	
	public MCCreditTransferResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MCCreditTransferResponse(String tranID, String tranDateTime) {
		super();
		TranID = tranID;
		TranDateTime = tranDateTime;
		
	}

	public String getTranID() {
		return TranID;
	}


	public void setTranID(String tranID) {
		TranID = tranID;
	}


	public String getTranDateTime() {
		return TranDateTime;
	}


	public void setTranDateTime(String tranDateTime) {
		TranDateTime = tranDateTime;
	}


	public String getStatus() {
		return Status;
	}


	public void setStatus(String status) {
		Status = status;
	}

}
