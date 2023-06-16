/**This class handles the Deactivation request of VoiceNote service and 
 * send request to rule engine for deactivation
 * @author Abhishek Rana
 */
package com.vcc.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.vcc.bean.RequestBean;
import com.vcc.bean.ResponseBean;
import com.vcc.config.AppConfig;
import com.vcc.config.TcpPool;

import edu.emory.mathcs.util.net.Connection;

public class VccVnUnsubscribe implements VccVnUnsub
{
	private static Logger logger = Logger.getLogger(VccVnUnsubscribe.class);
	String MSISDN;
	String tid;
	String lang;
	/**
	 * This method handles the Unsubcription request of VoiceNote service
	 * 
	 */
	@Override
	public String unSubscibe(String MSISDN, String tid,String lang) 
	{
		logger.info("Inside Unsubscribe method of VoiceNote Service ["+MSISDN+"]");

		this.MSISDN=MSISDN;
		this.tid=tid;
		this.lang= lang;
		
		String status = "";
		
		logger.info("MSISDN["+this.MSISDN+"] TransactionID["+this.tid+"] language["+this.lang+"]");
		
		if(this.MSISDN!=null && this.tid!=null)
		{
			if(this.lang==null || this.lang.equals(""))
			{
				this.lang="EN";
			}
			if(this.MSISDN.charAt(0)=='0')
			{
				this.MSISDN = this.MSISDN.substring(1);
			}
			if(!this.MSISDN.startsWith(AppConfig.config.getString("country_code")))
			{
				this.MSISDN = AppConfig.config.getString("country_code")+this.MSISDN;
			}
			logger.info("Going to Unsubscribe MSISDN["+this.MSISDN+"] TransactionID["+this.tid+"] language["+this.lang+"]");
			
			RequestBean requestBean = new RequestBean();
			requestBean.setMsisdn(this.MSISDN);
			if(this.lang.equalsIgnoreCase("EN"))
			{
				requestBean.setLang("1");
			}
			else if(this.lang.equalsIgnoreCase("AR"))
			{
				requestBean.setLang("2");
			}
			else
			{
				requestBean.setLang("1");
			}
			requestBean.setTid(this.tid);
			requestBean.setActionId(AppConfig.config.getString("unsub_actionId"));
			requestBean.setChannel(AppConfig.config.getString("default_channel"));
			requestBean.setInterFace(AppConfig.config.getString("default_interface"));
			requestBean.setServiceType(AppConfig.config.getString("VN"));
			requestBean.setAppId(AppConfig.config.getString("appId"));
			requestBean.setReqBy(this.MSISDN);
			requestBean.setActTrg("4");
	
			String json = new Gson().toJson(requestBean);
			String result = requestToVccRule(json);
			status = result;
		}
		else
		{
			logger.info("Unable to Unsubscribe MSISDN ["+this.MSISDN+"] TransactionID ["+this.tid+"]");
			status = "FAIL";
		}
		return status;
	}
	
	/**
	 * This method is used to send request to RuleEngine for activation and deactivation.
	 *  
	 * @param json contains the request paramaters which is send to rule engine in form of JSON
	 * @return response send by rule engine in form of String
	 *
	 */
	public String requestToVccRule(String json)
	{
		//logger.info("Going to request Rule Engine");
		Connection socketConnection = null;
		String response = null;
		edu.emory.mathcs.util.net.ConnectionPool connectionPool=null;
		Socket client=null;
		OutputStream outToServer=null;
		DataOutputStream out=null;
		InputStream inFromServer=null;
		DataInputStream in=null;
		String result="";   
		try 
		{
			connectionPool = TcpPool.getRuleEngineConPool();
			socketConnection = connectionPool.getConnection();
			
			if(connectionPool!=null && socketConnection!=null)
			{
				logger.info("Rule Engine Server has connected!\n");
	            logger.info("Rule Engine Sending string: '" + json + "'\n");
	            client = socketConnection.getSocket();
	            outToServer = client.getOutputStream();
	            out = new DataOutputStream(outToServer);
	            out.writeUTF(json.toString());
	            out.flush();
	            inFromServer = client.getInputStream();
	            in = new DataInputStream(inFromServer);
	            response = in.readUTF();
	            socketConnection.returnToPool();
	            
	            if (response != null) 
	            {   
	            	
	            	logger.info(" Rule Engine response is: " + response);
	            	ResponseBean responseBean = new Gson().fromJson(response, ResponseBean.class);
	            	result = "Result : "+responseBean.getResult()+" , Message :"+responseBean.getMsg();  	            	
	            }
	            else 
	            {
	                logger.info("Rule engine response is : " + response);
	            }
			}
			else
			{
				logger.error("Rule is not connected so can't be perform any opearation ");
			}
		}
		catch (Exception e) 
		{
			socketConnection.close();
			e.printStackTrace();
			
		}
		finally 
		{
			try 
			{
				if(out!=null)
					out.close();
				if(in!=null)
					in.close();
	        
			}
			catch (Exception e) 
			{
				logger.error("Error in closing socket connection ["+e+"]");
	        }
	    }
		return result;
	}

}
