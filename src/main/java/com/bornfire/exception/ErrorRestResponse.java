package com.bornfire.exception;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"ErrorCode","Description"})
public class ErrorRestResponse {
	private Integer ErrorCode;
	private String Description;
	@JsonProperty("ErrorCode")
	public Integer getErrorCode() {
		return ErrorCode;
	}
	@JsonProperty("ErrorCode")
	public void setErrorCode(Integer errorCode) {
		ErrorCode = errorCode;
	}
	@JsonProperty("Description")
	public String getDescription() {
		return Description;
	}
	@JsonProperty("Description")
	public void setDescription(String description) {
		Description = description;
	}
	
	public ErrorRestResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ErrorRestResponse(Integer errorCode, String description) {
		super();
		ErrorCode = errorCode;
		Description = description;
	}
	@Override
	public String toString() {
		return "ErrorRestResponse [ErrorCode=" + ErrorCode + ", Description=" + Description + "]";
	}
	
	
	
}
