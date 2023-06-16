package com.vcc.bean;

public class NotifyMeResponse
{
	private String msisdn;
	private String status;
	public String getMsisdn() 
	{
		return msisdn;
	}
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	public String getStatus() 
	{
		return status;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	public String toString() 
	{
        return "NotifyMeResponse [ msisdn=" + msisdn + ", Status=" + status + "]";
	}
	

}
