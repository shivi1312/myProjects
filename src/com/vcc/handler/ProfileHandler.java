package com.vcc.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;

import com.vcc.config.AppConfig;
import com.vcc.request.ProfileRequest;
import com.vcc.response.ProfileResponse;
import com.vcc.servicecaller.RequestSender;
import com.vcc.servicecaller.RftCaller;

public class ProfileHandler {

	final static Logger logger = Logger.getLogger(ProfileHandler.class);
	final static Logger errorLogger = Logger.getLogger("errorLogger");
	
	
	String request = AppConfig.config
			.getString(
					"request",
					"COMS");
	public void createProfile(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		if (profileRequest.getMsisdn() != null
				&& profileRequest.getMethodName() != null
				&& profileRequest.getTransactionId() != null
				&& profileRequest.getLanguage() != null
				&& profileRequest.getDemandValue() != null
				&& profileRequest.getServiceType() != null
				&& profileRequest.getIMSI() != null
				&& profileRequest.getActTrigger() != null
				&& profileRequest.getReceivedTime() != null
				&& profileRequest.getActionType() != null) {

			if (profileRequest.getInterFace().equalsIgnoreCase("irt")
					|| profileRequest.getInterFace().equalsIgnoreCase("irc")
					|| profileRequest.getInterFace().equalsIgnoreCase("ivr")) {
				profileRequest.setChannel(AppConfig.config.getString(
						"ivr_interface", "IVR-101"));
			} else if (profileRequest.getInterFace().equalsIgnoreCase("U")) {
				profileRequest.setChannel(AppConfig.config.getString(
						"ussd_interface", "USSD"));
			} else {
				profileRequest.setChannel(AppConfig.config.getString(
						"ussd_interface", "USSD"));
			}
			profileRequest.setPartNum(AppConfig.config.getString("part_num",
					"NA"));
			profileRequest.setChannelType(AppConfig.config.getString(
					"channel_type", "Direct Assist"));
			profileRequest.setAction(AppConfig.config.getString("action",
					"Query"));
			profileRequest.setOperation(AppConfig.config.getString("operation",
					"GetCustomerProfile"));
			profileRequest.setParentOrderNumber(AppConfig.config.getString(
					"parentOrderNumber", "-1"));
			profileRequest.setOrderItemNumber(AppConfig.config.getString(
					"orderItemNumber", "1"));
			profileRequest.setRequestedSystem(AppConfig.config.getString(
					"requested_system", "VCC"));
			profileRequest.setModeType(AppConfig.config.getString(
					profileRequest.getMethodName() + "."
							+ profileRequest.getServiceType() + ".mode",
					"Async"));

			this.setFormattedRecievedtime(profileRequest);
			if (AppConfig.config
					.getBoolean(profileRequest.getMethodName() + "."
							+ profileRequest.getServiceType() + ".eligible",
							true)) {
				logger.info("[" + profileRequest.getMsisdn() + "] ["
						+ profileRequest.getTransactionId()
						+ "] Going To Check Eligiblity method: ["
						+ profileRequest.getMethodName() + "]");
				this.checkEligibility(profileRequest, bindingResult,
						profileResponse);

			} else {
				logger.info("[" + profileRequest.getMsisdn() + "] ["
						+ profileRequest.getTransactionId()
						+ "] Bypass Check Eligibility method: ["
						+ profileRequest.getMethodName() + "]");
				profileResponse.setStatus("SUCCESS");
				profileResponse.setResponseCode("done");
			}
			if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")
					&& (profileRequest.getSubType() == null
							|| profileRequest.getSubType().equalsIgnoreCase("") || profileRequest
							.getSubType().equalsIgnoreCase("NA"))
					&& profileResponse.getResponseCode().equalsIgnoreCase(
							"done")) {
				logger.info("SubType is [" + profileRequest.getSubType() + "]");
				this.getAccountDetails(profileRequest, bindingResult,
						profileResponse);

				if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")) {

					this.createOrder(profileRequest, bindingResult,
							profileResponse);

					if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")) {
						logger.info("Request Successfully Proccessed. MSISDN ["
								+ profileRequest.getMsisdn()
								+ "] TransactionId["
								+ profileRequest.getTransactionId()
								+ "] Status [" + profileResponse.getStatus()
								+ "]");

					} else {
						logger.info("Unable to Process Request : MSISDN ["
								+ profileRequest.getMsisdn()
								+ "] TransactionId["
								+ profileRequest.getTransactionId()
								+ "] Status[" + profileResponse.getStatus()
								+ "] ErrorCode ["
								+ profileResponse.getErrorCode()
								+ "] ErrorType ["
								+ profileResponse.getErrorType() + "]");
						profileResponse.setStatus("FAIL");
					}
				}
			} else if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")
					&& (profileRequest.getSubType() != null && (!profileRequest
							.getSubType().equalsIgnoreCase("NA") || !profileRequest
							.getSubType().equalsIgnoreCase("")))
					&& profileResponse.getResponseCode().equalsIgnoreCase(
							"done")) {

				this.createOrder(profileRequest, bindingResult, profileResponse);

				if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")
						&& profileResponse.getResponseCode().equalsIgnoreCase(
								"done")) {
					logger.info("Request Successfully Proccessed. MSISDN ["
							+ profileRequest.getMsisdn() + "] TransactionId["
							+ profileRequest.getTransactionId() + "] Status ["
							+ profileResponse.getStatus() + "]");
				} else {
					logger.info("Unable to Process Request : MSISDN ["
							+ profileRequest.getMsisdn() + "] TransactionId["
							+ profileRequest.getTransactionId() + "] Status["
							+ profileResponse.getStatus() + "] ErrorCode ["
							+ profileResponse.getErrorCode() + "] ErrorType ["
							+ profileResponse.getErrorType() + "]");
					profileResponse.setStatus("FAIL");
				}
			} else {

				logger.info(" CheckEligibility Response : MSISDN["
						+ profileRequest.getMsisdn() + "] TransactionId["
						+ profileRequest.getTransactionId() + "] Status ["
						+ profileResponse.getStatus() + "] ErrorCode ["
						+ profileResponse.getErrorCode() + "] ErrorType ["
						+ profileResponse.getErrorType() + "]");
				profileResponse.setStatus("FAIL");
			}
		} else {
			errorLogger.error("ErrorCode ["
					+ AppConfig.config.getString("errorcode_pattern",
							"VCC-CRMC-") + "00002] MSISDN ["
					+ profileRequest.getMsisdn() + "] MethodName["
					+ profileRequest.getMethodName() + "] TransactionId ["
					+ profileRequest.getTransactionId() + "] Language ["
					+ profileRequest.getLanguage() + "] DemandValue ["
					+ profileRequest.getDemandValue() + "] serviceType ["
					+ profileRequest.getServiceType() + "] IMSI ["
					+ profileRequest.getIMSI() + "] ActiveTrigger ["
					+ profileRequest.getActTrigger() + "] ReceivedTime ["
					+ profileRequest.getReceivedTime() + "]  ActionType ["
					+ profileRequest.getActionType()
					+ "] [Mandatory Parameters are missing in request.]");
			logger.info("Some Parameters are missing. MSISDN ["
					+ profileRequest.getMsisdn() + "] MethodName["
					+ profileRequest.getMethodName() + "] TransactionId ["
					+ profileRequest.getTransactionId() + "] Language ["
					+ profileRequest.getLanguage() + "] DemandValue ["
					+ profileRequest.getDemandValue() + "] serviceType ["
					+ profileRequest.getServiceType() + "] IMSI ["
					+ profileRequest.getIMSI() + "] ActiveTrigger ["
					+ profileRequest.getActTrigger() + "] ReceivedTime ["
					+ profileRequest.getReceivedTime() + "]  ActionType ["
					+ profileRequest.getActionType() + "]");
			profileResponse.setStatus("FAIL");
		}
	}

	public void setDemandValue(ProfileRequest profileRequest) {
		logger.info("Before Setting Demand Value : MSISDN ["
				+ profileRequest.getMsisdn() + "] Demand value ["
				+ profileRequest.getDemandValue() + "] Req Type ["
				+ profileRequest.getReqType() + "] Action ["
				+ profileRequest.getActionType() + "]");
		if (profileRequest.getReqType().equalsIgnoreCase("create")) {
			if (profileRequest.getActionType().equalsIgnoreCase("AddService")) {
				if (profileRequest.getDemandValue().equalsIgnoreCase("VM EXEC")) {
					profileRequest.setDemandValue(AppConfig.config.getString(
							"voice_mail_exec_add", "61448"));
				} else if (profileRequest.getDemandValue().equalsIgnoreCase(
						"VM BASIC")) {
					profileRequest.setDemandValue(AppConfig.config.getString(
							"voice_mail_basic_add", "61450"));
				} else if (profileRequest.getDemandValue().equalsIgnoreCase(
						"VN")) {
					profileRequest.setDemandValue(AppConfig.config.getString(
							"voice_note_add", "61453"));
				}
			} else if (profileRequest.getActionType().equalsIgnoreCase(
					"DeleteService")) {
				if (profileRequest.getDemandValue().equalsIgnoreCase("VM EXEC")) {
					profileRequest.setDemandValue(AppConfig.config.getString(
							"voice_mail_exec_delete", "61449"));
				} else if (profileRequest.getDemandValue().equalsIgnoreCase(
						"VM BASIC")) {
					profileRequest.setDemandValue(AppConfig.config.getString(
							"voice_mail_basic_delete", "61451"));
				} else if (profileRequest.getDemandValue().equalsIgnoreCase(
						"VN")) {
					profileRequest.setDemandValue(AppConfig.config.getString(
							"voice_note_delete", "61454"));
				} else if (profileRequest.getDemandValue().equalsIgnoreCase(
						"notifyMe")) {
					profileRequest.setDemandValue(AppConfig.config.getString(
							"notifyme_delete", "73191"));
				}
			}
		} else if (profileRequest.getReqType().equalsIgnoreCase("modify")) {
			if (profileRequest.getMethodName().equalsIgnoreCase("AddService")) {
				if (profileRequest.getDemandValue().equalsIgnoreCase("VM EXEC")) {
					profileRequest.setDemandValue(AppConfig.config.getString(
							"voice_mail_exec_modify", "100004"));
				}if (profileRequest.getDemandValue().equalsIgnoreCase("VM BASIC")) {
					profileRequest.setDemandValue(AppConfig.config.getString(
							"voice_mail_basic_modify", "61452"));
				}
				else if (profileRequest.getDemandValue().equalsIgnoreCase(
						"VN")) {
					profileRequest.setDemandValue(AppConfig.config.getString(
							"voice_note_modify", "61455"));
				}
			}
		}
		logger.info("After Setting Demand Value : MSISDN ["
				+ profileRequest.getMsisdn() + "] Demand value ["
				+ profileRequest.getDemandValue() + "] Req Type ["
				+ profileRequest.getReqType() + "] Action ["
				+ profileRequest.getActionType() + "]");
	}

	public void checkEligibility(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		if (profileRequest.getMsisdn() != null
				&& profileRequest.getTransactionId() != null
				&& profileRequest.getActionType() != null
				&& profileRequest.getLanguage() != null
				&& profileRequest.getDemandValue() != null
				&& profileRequest.getReceivedTime() != null) {

			this.setDemandValue(profileRequest);

			if (profileRequest.getActionType().equalsIgnoreCase("AddService")) {
				profileRequest.setActionType(AppConfig.config.getString(
						"add_actiontype", "ADDSRVICE"));
			} else if (profileRequest.getActionType().equalsIgnoreCase(
					"DeleteService")) {
				profileRequest.setActionType(AppConfig.config.getString(
						"del_actiontype", "DELETESRV"));

			}
			profileRequest.setPartNum(AppConfig.config.getString("part_num",
					"NA"));

			profileRequest.setSubChannel(AppConfig.config.getString(
					"subchannel", "CBCM"));

			profileRequest.setRequestedSystem(AppConfig.config.getString(
					"requested_system", "VCC"));
			profileRequest.setDemandId(AppConfig.config.getString("demand_id",
					"demand_id"));

			profileRequest.setParentOrderNumber(AppConfig.config.getString(
					"parentOrderNumber", "-1"));
			profileRequest.setOrderItemNumber(AppConfig.config.getString(
					"orderItemNumber", "1"));
			profileRequest.setAction(AppConfig.config.getString("action",
					"Query"));
			profileRequest.setOperation(AppConfig.config.getString("operation",
					"GetCustomerProfile"));

			if (profileRequest.getLanguage().equalsIgnoreCase("1")) {
				profileRequest.setLanguage(AppConfig.config.getString("lang.1",
						"EN"));
			} else if (profileRequest.getLanguage().equalsIgnoreCase("2")) {
				profileRequest.setLanguage(AppConfig.config.getString("lang.2",
						"AR"));
			} else {
				profileRequest.setLanguage(AppConfig.config.getString("lang.1",
						"EN"));
			}
	logger.info("profileRequest done :::::::::::::profileRequest ="+profileRequest.getAction()+"    ,     getActionType ="+profileRequest.getActionType()+"      "
			+ ",  profileRequest.getPartNum  ="+profileRequest.getPartNum()+"  ,profileRequest.getOperation ="+profileRequest.getOperation()+"  ,profileRequest.getLanguage ="
			+profileRequest.getLanguage()+"  ,DemandId= "+profileRequest.getDemandId()+ "  ,profileRequest.SubChannel ="+profileRequest.getSubChannel()
			+"  ,profileRequest.RequestedSystem ="+profileRequest.getRequestedSystem());
	
	logger.info("profileRequest done ");
		 
	        switch (request) {
	 
	        case "RFT":
	 
	            logger.info("RFT");
	            logger.info("request set -------------------------------"+request);
	            RftCaller rftCaller =new RftCaller();
	            rftCaller.checkEligibility(profileRequest, profileResponse);
	            
	            break;
	           default : 
	        	   logger.info("request set -------------------------------"+request);
			RequestSender requestSender = new RequestSender();
			requestSender.checkEligibility(profileRequest, profileResponse);
	        }
	        
	        
	        
		} else {
			logger.info("Some Parameters are missing. MSISDN ["
					+ profileRequest.getMsisdn() + "] ActionType["
					+ profileRequest.getActionType() + "]  TransactionId ["
					+ profileRequest.getTransactionId() + "] Language ["
					+ profileRequest.getLanguage() + "] Channel ["
					+ profileRequest.getChannel() + "]   DemandValue ["
					+ profileRequest.getDemandValue() + "]  ReceivedTime ["
					+ profileRequest.getReceivedTime() + "]");
			profileResponse.setStatus("FAIL");
		}
	}


	public void getSubType(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		if (profileRequest.getMsisdn() != null
				&& profileRequest.getTransactionId() != null) {

			profileRequest.setAction(AppConfig.config.getString("action",
					"Query"));
			profileRequest.setOperation(AppConfig.config.getString("operation",
					"GetCustomerProfile"));
			profileRequest.setRequestedSystem(AppConfig.config.getString(
					"requested_system", "VCC"));
			profileRequest.setActionType(AppConfig.config.getString(
					"action_type", "GetBalanceAndDate"));
			profileRequest.setModeType("Sync");
			RequestSender requestSender = new RequestSender();
			// requestSender.getSubType(profileRequest, profileResponse);
			requestSender.getAccountDetail(profileRequest, profileResponse);
		} else {
			logger.info("Some Parameters are missing. MSISDN["
					+ profileRequest.getMsisdn() + "] TransactionId ["
					+ profileRequest.getTransactionId() + "] IMSI ["
					+ profileRequest.getIMSI() + "] ");
			profileResponse.setStatus("FAIL");
		}
	}

	public void getAccountDetails(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {
		if (profileRequest.getMsisdn() != null
				&& profileRequest.getTransactionId() != null
				&& profileRequest.getNoOfAccounts() != null) {

			profileRequest.setRequestedSystem(AppConfig.config.getString(
					"requested_system", "VCC"));
			profileRequest.setActionType(AppConfig.config.getString(
					"action_type", "GetBalanceAndDate"));
			profileRequest.setModeType("Sync");

			RequestSender requestSender = new RequestSender();
			requestSender.getAccountDetail(profileRequest, profileResponse);
		} else {
			logger.info("Some Parameters are missing. MSISDN["
					+ profileRequest.getMsisdn() + "]  TransactionId ["
					+ profileRequest.getTransactionId() + "] NoOfAccounts ["
					+ profileRequest.getNoOfAccounts() + "] ");
			profileResponse.setStatus("FAIL");
		}
	}

	public void createOrder(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		if (profileRequest.getMsisdn() != null) {

			profileRequest.setDemandId(AppConfig.config.getString(
					"create_demand_id", "DEMAND_ID"));
			this.setDemandValue(profileRequest); 

			RequestSender requestSender = new RequestSender();
			requestSender.createOrder(profileRequest, profileResponse);

		} else {
			logger.info("Some Parameters are missing. MSISDN["
					+ profileRequest.getMsisdn() + "] ");
		}
	}

	public void getActiveTrigger(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		if (profileRequest.getMsisdn() != null
				&& profileRequest.getTransactionId() != null
				&& profileRequest.getIMSI() != null) {

			profileRequest.setAction(AppConfig.config.getString("action",
					"Query"));
			profileRequest.setOperation(AppConfig.config.getString("operation",
					"GetCustomerProfile"));
			profileRequest.setRequestedSystem(AppConfig.config.getString(
					"requested_system", "VCC"));

			RequestSender requestSender = new RequestSender();
			requestSender.getActiveTriggers(profileRequest, profileResponse);
		} else {
			logger.info("Some Parameters are missing. MSISDN["
					+ profileRequest.getMsisdn() + "] TransactionId ["
					+ profileRequest.getTransactionId() + "] IMSI ["
					+ profileRequest.getIMSI() + "] ");
			profileResponse.setStatus("FAIL");
		}
	}

	public void modifyProfile(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		if (profileRequest.getMsisdn() != null
				&& profileRequest.getMethodName() != null
				&& profileRequest.getTransactionId() != null
				&& profileRequest.getLanguage() != null
				&& profileRequest.getDemandValue() != null
				&& profileRequest.getServiceType() != null
				&& profileRequest.getIMSI() != null
				&& profileRequest.getActTrigger() != null
				&& profileRequest.getReceivedTime() != null
				&& profileRequest.getActionType() != null) {

			if (profileRequest.getInterFace().equalsIgnoreCase("irt")
					|| profileRequest.getInterFace().equalsIgnoreCase("irc")
					|| profileRequest.getInterFace().equalsIgnoreCase("ivr")) {
				profileRequest.setChannel(AppConfig.config.getString(
						"ivr_interface", "IVR-101"));
			} else if (profileRequest.getInterFace().equalsIgnoreCase("U")) {
				profileRequest.setChannel(AppConfig.config.getString(
						"ussd_interface", "USSD"));
			} else {
				profileRequest.setChannel(AppConfig.config.getString(
						"ussd_interface", "USSD"));
			}
			profileRequest.setPartNum(AppConfig.config.getString("part_num",
					"NA"));
			profileRequest.setChannelType(AppConfig.config.getString(
					"channel_type", "Direct Assist"));
			profileRequest.setAction(AppConfig.config.getString("action",
					"Query"));
			profileRequest.setOperation(AppConfig.config.getString("operation",
					"GetCustomerProfile"));
			profileRequest.setParentOrderNumber(AppConfig.config.getString(
					"parentOrderNumber", "-1"));
			profileRequest.setOrderItemNumber(AppConfig.config.getString(
					"orderItemNumber", "1"));
			profileRequest.setRequestedSystem(AppConfig.config.getString(
					"requested_system", "VCC"));
			profileRequest.setModeType("Async");

			
			if (AppConfig.config.getBoolean(profileRequest.getReqType()+".eligible",true)) {
				logger.info("[" + profileRequest.getMsisdn() + "] ["
						+ profileRequest.getTransactionId()
						+ "] Going To Check Eligiblity method: ["
						+ profileRequest.getMethodName() + "]");
				this.checkEligibility(profileRequest, bindingResult,
						profileResponse);

			} else {
				logger.info("[" + profileRequest.getMsisdn() + "] ["
						+ profileRequest.getTransactionId()
						+ "] Bypass Check Eligibility method");
				profileResponse.setStatus("SUCCESS");
				profileResponse.setResponseCode("done");
			}

			if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")
					&& (profileRequest.getSubType() == null
							|| profileRequest.getSubType().equalsIgnoreCase("") || profileRequest
							.getSubType().equalsIgnoreCase("NA"))
					&& profileResponse.getResponseCode().equalsIgnoreCase(
							"done")) {
				logger.info("SubType is [" + profileRequest.getSubType() + "]");
				this.getAccountDetails(profileRequest, bindingResult,
						profileResponse);

				if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")) {

					this.createOrder(profileRequest, bindingResult, profileResponse);

					if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")) {
						logger.info("Request Successfully Proccessed. MSISDN ["
								+ profileRequest.getMsisdn() + "] Status ["
								+ profileResponse.getStatus() + "]");

					} else {
						logger.info("Unable to Process Request : MSISDN ["
								+ profileRequest.getMsisdn() + "] Status["
								+ profileResponse.getStatus() + "] ErrorCode ["
								+ profileResponse.getErrorCode()
								+ "] ErrorType ["
								+ profileResponse.getErrorType() + "]");
					}
				} else {
					logger.info("getSubType response : Status["
							+ profileResponse.getStatus() + "] ErrorCode ["
							+ profileResponse.getErrorCode() + "] ErrorType ["
							+ profileResponse.getErrorType() + "]");
				}
			} else if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")
					&& !profileRequest.getSubType().equalsIgnoreCase("NA")
					&& profileResponse.getResponseCode().equalsIgnoreCase(
							"done")) {
				logger.info("SubType is [" + profileRequest.getSubType() + "]");

				this.createOrder(profileRequest, bindingResult, profileResponse);

				if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")
						&& profileResponse.getResponseCode().equalsIgnoreCase(
								"done")) {
					logger.info("Request Successfully Proccessed. MSISDN ["
							+ profileRequest.getMsisdn() + "] Status ["
							+ profileResponse.getStatus() + "]");
				} else {
					logger.info("Unable to Process Request : MSISDN ["
							+ profileRequest.getMsisdn() + "] Status["
							+ profileResponse.getStatus() + "] ErrorCode ["
							+ profileResponse.getErrorCode() + "] ErrorType ["
							+ profileResponse.getErrorType() + "]");
				}
			} else {
				logger.info(" CheckEligibility Response : Status ["
						+ profileResponse.getStatus() + "] ErrorCode ["
						+ profileResponse.getErrorCode() + "] ErrorType ["
						+ profileResponse.getErrorType() + "]");
			}
		} else {
			logger.info("Some Parameters are missing. MSISDN ["
					+ profileRequest.getMsisdn() + "] MethodName["
					+ profileRequest.getMethodName() + "] TransactionId ["
					+ profileRequest.getTransactionId() + "] Language ["
					+ profileRequest.getLanguage() + "] DemandValue ["
					+ profileRequest.getDemandValue() + "] serviceType ["
					+ profileRequest.getServiceType() + "] IMSI ["
					+ profileRequest.getIMSI() + "] ActiveTrigger ["
					+ profileRequest.getActTrigger() + "] ReceivedTime ["
					+ profileRequest.getReceivedTime() + "]  ActionType ["
					+ profileRequest.getActionType() + "]");
			profileResponse.setStatus("FAIL");
		}
	}

	public void updateUserProfile(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {
		if (profileRequest.getMsisdn() != null
				&& profileRequest.getTransactionId() != null) {
			RequestSender requestSender = new RequestSender();
			requestSender.updateProfile(profileRequest, profileResponse);
		} else {
			logger.info("Some Parameters are missing. MSISDN["
					+ profileRequest.getMsisdn() + "] TransactionId ["
					+ profileRequest.getTransactionId() + "] ");
			profileResponse.setStatus("FAIL");
		}
	}

	public void setFormattedRecievedtime(ProfileRequest profileRequest) {
		try {
			Date currentDate = new Date();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat HourFormatter = new SimpleDateFormat("HH:mm:ss");
			String formattedDate = dateFormatter.format(currentDate);
			String formattedHour = HourFormatter.format(currentDate);
			String formattedRecievedTime = formattedDate + "T" + formattedHour;
			logger.info("MSISDN[" + profileRequest.getMsisdn()
					+ "] TransactionId[" + profileRequest.getTransactionId()
					+ "] RecievedTime[" + formattedRecievedTime + "]");
			profileRequest.setReceivedTime(formattedRecievedTime);
		} catch (Exception e) {
			errorLogger
					.error("ErrorCode ["
							+ AppConfig.config.getString("errorcode_pattern",
									"VCC-CRMC-")
							+ "00008] MSISDN ["
							+ profileRequest.getMsisdn()
							+ "] TransactionId ["
							+ profileRequest.getTransactionId()
							+ "] [Exception while setting Formatted Recieved Time] Error["
							+ e.getMessage() + "]");
			logger.info("MSISDN[" + profileRequest.getMsisdn()
					+ "] TransactionId[" + profileRequest.getTransactionId()
					+ "] Exception while setting Formatted Recieved Time" + e);
		}
	}

	public void notifymeOptOut(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {
		if (profileRequest.getMsisdn() != null
				&& profileRequest.getLanguage() != null) {
			if (profileRequest.getLanguage().equalsIgnoreCase("1")) {
				profileRequest.setLanguage(AppConfig.config.getString("lang.1",
						"EN"));
			} else if (profileRequest.getLanguage().equalsIgnoreCase("2")) {
				profileRequest.setLanguage(AppConfig.config.getString("lang.2",
						"AR"));
			} else {
				profileRequest.setLanguage(AppConfig.config.getString("lang.1",
						"EN"));
			}

			profileRequest.setChannel(AppConfig.config.getString(
					"ussd_interface", "USSD"));

			profileRequest.setDemandId(AppConfig.config.getString(
					"create_demand_id", "DEMAND_ID"));
			profileRequest.setDemandValue(AppConfig.config.getString(
					"notifyme_delete", "1009825"));
			profileRequest.setMethodName(AppConfig.config.getString(
					"methodname_del", "DeleteService"));
			profileRequest.setActionType(AppConfig.config.getString(
					"actionname_del", "DeleteService"));
			profileRequest.setPartNum(AppConfig.config.getString("part_num",
					"NA"));
			profileRequest.setChannelType(AppConfig.config.getString(
					"channel_type", "Direct Assist"));
			profileRequest.setAction(AppConfig.config.getString("action",
					"Query"));
			profileRequest.setRequestedSystem(AppConfig.config.getString(
					"requested_system", "VCC"));
			profileRequest.setModeType(AppConfig.config.getString(
					profileRequest.getMethodName() + "."
							+ profileRequest.getServiceType() + ".mode",
					"Async"));
			RequestSender requestSender = new RequestSender();
			requestSender.notifyMeOptInOptOut(profileRequest, profileResponse);
			if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")
					&& profileResponse.getResponseCode().equalsIgnoreCase(
							"done")) {
				logger.info("Request Successfully Proccessed. MSISDN ["
						+ profileRequest.getMsisdn() + "] TransactionId["
						+ profileRequest.getTransactionId() + "] Status ["
						+ profileResponse.getStatus() + "]");
			} else {
				logger.info("Unable to Process Request : MSISDN ["
						+ profileRequest.getMsisdn() + "] TransactionId["
						+ profileRequest.getTransactionId() + "] Status["
						+ profileResponse.getStatus() + "] ErrorCode ["
						+ profileResponse.getErrorCode() + "] ErrorType ["
						+ profileResponse.getErrorType() + "]");
				profileResponse.setStatus("FAIL");
			}
		} else {
			logger.info("Some Parameters are missing. MSISDN["
					+ profileRequest.getMsisdn() + "] Language["
					+ profileRequest.getLanguage() + "]");
			profileResponse.setStatus("FAIL");
		}
	}
	public void notifymeOptIn(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {
		if (profileRequest.getMsisdn() != null
				&& profileRequest.getLanguage() != null) {
			if (profileRequest.getLanguage().equalsIgnoreCase("1")) {
				profileRequest.setLanguage(AppConfig.config.getString("lang.1",
						"EN"));
			} else if (profileRequest.getLanguage().equalsIgnoreCase("2")) {
				profileRequest.setLanguage(AppConfig.config.getString("lang.2",
						"AR"));
			} else {
				profileRequest.setLanguage(AppConfig.config.getString("lang.1",
						"EN"));
			}

			profileRequest.setChannel(AppConfig.config.getString(
					"ussd_interface", "USSD"));

			profileRequest.setDemandId(AppConfig.config.getString(
					"create_demand_id", "DEMAND_ID"));
			profileRequest.setDemandValue(AppConfig.config.getString(
					"notifyme_add", "1009823"));
			profileRequest.setMethodName(AppConfig.config.getString(
					"methodname_add", "AddService"));
			profileRequest.setActionType(AppConfig.config.getString(
					"actionname_add", "AddService"));
			profileRequest.setPartNum(AppConfig.config.getString("part_num",
					"NA"));
			profileRequest.setChannelType(AppConfig.config.getString(
					"channel_type", "Direct Assist"));
			profileRequest.setAction(AppConfig.config.getString("action",
					"Query"));
			profileRequest.setRequestedSystem(AppConfig.config.getString(
					"requested_system", "VCC"));
			profileRequest.setModeType(AppConfig.config.getString(
					profileRequest.getMethodName() + "."
							+ profileRequest.getServiceType() + ".mode",
					"Async"));
			RequestSender requestSender = new RequestSender();
			requestSender.notifyMeOptInOptOut(profileRequest, profileResponse);
			if (profileResponse.getStatus().equalsIgnoreCase("SUCCESS")
					&& profileResponse.getResponseCode().equalsIgnoreCase(
							"done")) {
				logger.info("Request Successfully Proccessed. MSISDN ["
						+ profileRequest.getMsisdn() + "] TransactionId["
						+ profileRequest.getTransactionId() + "] Status ["
						+ profileResponse.getStatus() + "]");
			} else {
				logger.info("Unable to Process Request : MSISDN ["
						+ profileRequest.getMsisdn() + "] TransactionId["
						+ profileRequest.getTransactionId() + "] Status["
						+ profileResponse.getStatus() + "] ErrorCode ["
						+ profileResponse.getErrorCode() + "] ErrorType ["
						+ profileResponse.getErrorType() + "]");
				profileResponse.setStatus("FAIL");
			}
		} else {
			logger.info("Some Parameters are missing. MSISDN["
					+ profileRequest.getMsisdn() + "] Language["
					+ profileRequest.getLanguage() + "]");
			profileResponse.setStatus("FAIL");
		}
	}

}