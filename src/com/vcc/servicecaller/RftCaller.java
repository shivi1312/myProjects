package com.vcc.servicecaller;

import java.net.URL;

import org.apache.log4j.Logger;

import com.vcc.config.AppConfig;
import com.vcc.request.ProfileRequest;
import com.vcc.response.ProfileResponse;


public class RftCaller {

	final static Logger logger = Logger.getLogger(RftCaller.class);
	final static Logger errorLogger = Logger.getLogger("errorLogger");
	final static Logger crmLogger = Logger.getLogger("crmLogger");

	public void checkEligibility(ProfileRequest profileRequest,
			ProfileResponse profileResponse) {
		
	
	System.out.println("CheckEligibility : TransactionID ["
			+ profileRequest.getTransactionId() + "] MSISDN ["
			+ profileRequest.getMsisdn() + "] ActionType ["
			+ profileRequest.getActionType() + "] Language ["
			+ profileRequest.getLanguage() + "]");

	String urlhit = AppConfig.config
			.getString(
					"autofulfilment_url",
					"https://mwsit.etisalat.ae:9157/Etisalat/Falcon/Tibco/IIT/DOM_CC/CustomerManagement/BS_SelfCareService");
	URL url = null;

	//CheckEligibilityRequest checkEligibilityRequest = new CheckEligibilityRequest();

	//ApplicationHeader applicationHeader = new ApplicationHeader();
	
	applicationHeader.setRequestedDate(profileRequest.getRequestedDate());
	applicationHeader.setRequestedSystem(profileRequest.getRequestedSystem());
	applicationHeader.setRetryLimit(profileRequest.getRetryLimit());
	applicationHeader.setTransactionID(profileRequest.getTransactionId());
	
	
	
	System.out.println("applicationHeader======"+applicationHeader.getRequestedDate());

//	CheckEligibilityRequestDataHeader dataHeader = new CheckEligibilityRequestDataHeader();

	dataHeader.setChannel(profileRequest.getChannel());

	dataHeader.setSubChannel(profileRequest.getSubChannel());
	dataHeader.setLanguage(profileRequest.getLanguage());
	dataHeader.setReceivedTime(profileRequest.getReceivedTime());

	//CheckEligibilityRequestDataHeaderOrderItem[] dataHeaderOrderArray = new CheckEligibilityRequestDataHeaderOrderItem[1];

	//CheckEligibilityRequestDataHeaderOrderItem dataHeaderOrderItem = new CheckEligibilityRequestDataHeaderOrderItem();
	
	dataHeaderOrderItem.setAccountNumber(profileRequest.getMsisdn());
	dataHeaderOrderItem.setActionType(profileRequest.getActionType());
	dataHeaderOrderItem.setParentOrderNumber(Integer
			.parseInt(profileRequest.getParentOrderNumber()));
	dataHeaderOrderItem.setOrderItemNumber(Integer.parseInt(profileRequest
			.getOrderItemNumber()));

	dataHeaderOrderArray[0] = dataHeaderOrderItem;
	dataHeader.setOrderItem(dataHeaderOrderArray);

//	CheckEligibilityRequestDataHeaderOrderItemOfferID orderItemOfferID = new CheckEligibilityRequestDataHeaderOrderItemOfferID();
	orderItemOfferID.setName(profileRequest.getDemandId());
	orderItemOfferID.setValue(profileRequest.getDemandValue());

	//CheckEligibilityRequestDataHeaderOrderItemOfferID[] dataHeaderOrderItemOfferID = new CheckEligibilityRequestDataHeaderOrderItemOfferID[1];
	dataHeaderOrderItemOfferID[0] = orderItemOfferID;
	dataHeaderOrderItem.setOfferID(dataHeaderOrderItemOfferID);

	CheckEligibilityRequestDataHeaderOrderItemOrderInformation orderInformation = new CheckEligibilityRequestDataHeaderOrderItemOrderInformation();
	orderInformation.setAttributeName("user_id");
	orderInformation.setAttributeValue(AppConfig.config.getString(
			"user_id", "Channels"));

	CheckEligibilityRequestDataHeaderOrderItemOrderInformation[] itemOrderInformations = new CheckEligibilityRequestDataHeaderOrderItemOrderInformation[1];
	itemOrderInformations[0] = orderInformation;
	dataHeaderOrderItem.setOrderInformation(itemOrderInformations);

	checkEligibilityRequest.setApplicationHeader(applicationHeader);
	checkEligibilityRequest.setDataHeader(dataHeader);

	
	try {
		url = new URL(urlhit);
		System.out.println("MSISDN[" + profileRequest.getMsisdn()
				+ "] TransactionId[" + profileRequest.getTransactionId()
				+ "] URL for CheckEligibility[" + url + "]");
		
	/*	profileResponse.setStatus(checkEligibilityResponse.getAckMessage()
				.getStatus().getValue());
		profileResponse.setResponseCode(checkEligibilityResponse
				.getResponseData().getResponseCode());
		if (checkEligibilityResponse.getAckMessage().getErrorCode() != null) {
			profileResponse.setErrorCode(checkEligibilityResponse
					.getAckMessage().getErrorCode());
			profileResponse.setErrorType(checkEligibilityResponse
					.getAckMessage().getErrorType());
			profileResponse.setErrorDescription(checkEligibilityResponse
					.getAckMessage().getErrorDescription());
		}
		profileResponse.setAccountNumber(checkEligibilityResponse
				.getResponseData().getOrderItemResponse(0)
				.getAccountNumber());
*/
	/*
	 * System.out.println("Response from CheckEligibility. TransactionID [" +
	 * profileRequest.getTransactionId() + "] MSISDN[" + profileRequest.getMsisdn()
	 * + "] Status [" + checkEligibilityResponse.getAckMessage().getStatus() +
	 * "] ResponseCode[" + profileResponse.getResponseCode() +
	 * "] ResponseDescription[" + profileResponse.getResponseDescription() + "]");
	 */

	} catch (Exception e) {
		errorLogger
				.error("ErrorCode ["
						+ AppConfig.config.getString("errorcode_pattern",
								"VCC-CRMC-")
						+ "00005] MSISDN["
						+ profileRequest.getMsisdn()
						+ "] MethodName["
						+ profileRequest.getMethodName()
						+ "] TransactionId ["
						+ profileRequest.getTransactionId()
						+ "] Language ["
						+ profileRequest.getLanguage()
						+ "] DemandValue ["
						+ profileRequest.getDemandValue()
						+ "] [Exception while sending CheckEligibility request to Tibaco Interface] Error["
						+ e.getMessage() + "] ");
		e.printStackTrace();
		profileResponse.setStatus("FAIL");
		// profileResponse.setStatus("SUCCESS");
	}
	// crmSystem.out.println("{req:" + crmRequest + ",resp:" + crmResponse + "}");

	/*
	 * public void createOrder(ProfileRequest profileRequest, ProfileResponse
	 * profileResponse) {
	 * 
	 * logger.info("CreateOrder : TransactionId [" +
	 * profileRequest.getTransactionId() + "]  MethodName [" +
	 * profileRequest.getMethodName() + "] PartNum [" + profileRequest.getPartNum()
	 * + "] MSISDN [" + profileRequest.getMsisdn() + "] prefferedLanuage [" +
	 * profileRequest.getLanguage() + "] Channel [" + profileRequest.getChannel() +
	 * "] ChannelType [" + profileRequest.getChannelType() + "] "); // String urlhit
	 * =
	 */		// "http://www.tibco.com/Middleware/AutoServiceFulfilment/";
		/*
		 * String urlhit = AppConfig.config .getString( "autofulfilment_url",
		 * "https://mwsit.etisalat.ae:9157/Etisalat/Falcon/Tibco/IIT/DOM_CC/CustomerManagement/BS_SelfCareService"
		 * ); URL url = null;
		 * 
		 * ProvisionRequestedServiceRequest provisionRequestedServiceRequest = new
		 * ProvisionRequestedServiceRequest();
		 * 
		 * provisionRequestedServiceRequest.setTransaction_id(profileRequest
		 * .getTransactionId());
		 * provisionRequestedServiceRequest.setPreferred_language(profileRequest
		 * .getLanguage()); provisionRequestedServiceRequest.setPart_num(profileRequest
		 * .getPartNum()); provisionRequestedServiceRequest.setMode_type(profileRequest
		 * .getModeType());
		 * provisionRequestedServiceRequest.setMethod_name(profileRequest
		 * .getMethodName()); // for add order //
		 * provisionRequestedServiceRequest.setMethod_name("DeleteService"); // // for
		 * delete order
		 * 
		 * provisionRequestedServiceRequest.setMsisdn(profileRequest.getMsisdn());
		 * 
		 * ChannelInformation channelInformation = new ChannelInformation();
		 * 
		 * channelInformation.setChannel(profileRequest.getChannel());
		 * 
		 * channelInformation.setChannelType(profileRequest.getChannelType());
		 * provisionRequestedServiceRequest .setChannelInformation(channelInformation);
		 * 
		 * Additional_info[] additional_info = null;
		 * 
		 * Additional_info additionalInfoBusy = null; Additional_info
		 * additionalInfoNoReply = null; Additional_info additionalInfoNotReach = null;
		 * Additional_info additionalInfoUnConditional = null; Additional_info
		 * additionalInfoDemandValue = new Additional_info(); Additional_information
		 * additional_information = new Additional_information();
		 * additionalInfoDemandValue.setName(profileRequest.getDemandId());
		 * additionalInfoDemandValue.setValue(profileRequest.getDemandValue());
		 * Additional_info additionalInfoUserId = new Additional_info(); Additional_info
		 * additionalInfoRegionId = new Additional_info();
		 * additionalInfoUserId.setName("user_id");
		 * additionalInfoUserId.setValue(AppConfig.config.getString("user_id",
		 * "Channels")); additionalInfoRegionId.setName("region_id");
		 * additionalInfoRegionId.setValue("1"); if
		 * (profileRequest.getMethodName().equalsIgnoreCase("AddService")) { if
		 * (profileRequest.getServiceType().equalsIgnoreCase("VM") ||
		 * profileRequest.getServiceType().equalsIgnoreCase(
		 * AppConfig.config.getString("VM", "0010"))) { if
		 * (profileRequest.getActTrigger().equalsIgnoreCase("1")) { additionalInfoBusy =
		 * new Additional_info(); additionalInfoBusy.setName("CFB");
		 * //additionalInfoBusy.setValue("TRUE");
		 * additionalInfoBusy.setValue(""+profileRequest.isActVal()); additional_info =
		 * new Additional_info[4]; additional_info[0] = additionalInfoDemandValue;
		 * additional_info[1] = additionalInfoBusy; additional_info[2] =
		 * additionalInfoUserId; additional_info[3] = additionalInfoRegionId;
		 * additional_information.setAdditional_info(additional_info);
		 * provisionRequestedServiceRequest
		 * .setAdditional_information(additional_information);
		 * 
		 * } else if (profileRequest.getActTrigger().equalsIgnoreCase("2")) {
		 * additionalInfoNotReach = new Additional_info();
		 * additionalInfoNotReach.setName("CFNRC");
		 * //additionalInfoNotReach.setValue("TRUE");
		 * additionalInfoNotReach.setValue(""+profileRequest.isActVal());
		 * additional_info = new Additional_info[4]; additional_info[0] =
		 * additionalInfoDemandValue; additional_info[1] = additionalInfoNotReach;
		 * additional_info[2] = additionalInfoUserId; additional_info[3] =
		 * additionalInfoRegionId;
		 * additional_information.setAdditional_info(additional_info);
		 * provisionRequestedServiceRequest
		 * .setAdditional_information(additional_information);
		 * 
		 * } else if (profileRequest.getActTrigger().equalsIgnoreCase("3")) {
		 * additionalInfoNoReply = new Additional_info();
		 * additionalInfoNoReply.setName("CFNRY");
		 * //additionalInfoNoReply.setValue("TRUE");
		 * additionalInfoNoReply.setValue(""+profileRequest.isActVal()); additional_info
		 * = new Additional_info[4]; additional_info[0] = additionalInfoDemandValue;
		 * additional_info[1] = additionalInfoNoReply; additional_info[2] =
		 * additionalInfoUserId; additional_info[3] = additionalInfoRegionId;
		 * additional_information.setAdditional_info(additional_info);
		 * provisionRequestedServiceRequest
		 * .setAdditional_information(additional_information);
		 * 
		 * } else if (profileRequest.getActTrigger().equalsIgnoreCase("4")) {
		 * additionalInfoBusy = new Additional_info(); additionalInfoNoReply = new
		 * Additional_info(); additionalInfoNotReach = new Additional_info();
		 * additionalInfoUnConditional = new Additional_info();
		 * additionalInfoBusy.setName("CFB"); //additionalInfoBusy.setValue("TRUE");
		 * additionalInfoBusy.setValue(""+profileRequest.isActVal());
		 * additionalInfoNoReply.setName("CFNRY");
		 * //additionalInfoNoReply.setValue("TRUE");
		 * additionalInfoNoReply.setValue(""+profileRequest.isActVal());
		 * additionalInfoNotReach.setName("CFNRC");
		 * //additionalInfoNotReach.setValue("TRUE");
		 * additionalInfoNotReach.setValue(""+profileRequest.isActVal());
		 * additionalInfoUnConditional.setName("CFU");
		 * additionalInfoUnConditional.setValue("FALSE"); additional_info = new
		 * Additional_info[7]; additional_info[0] = additionalInfoDemandValue;
		 * additional_info[1] = additionalInfoBusy; additional_info[2] =
		 * additionalInfoNoReply; additional_info[3] = additionalInfoNotReach;
		 * additional_info[4] = additionalInfoUnConditional; additional_info[5] =
		 * additionalInfoUserId; additional_info[6] = additionalInfoRegionId;
		 * additional_information.setAdditional_info(additional_info);
		 * provisionRequestedServiceRequest
		 * .setAdditional_information(additional_information);
		 * 
		 * } } if (profileRequest.getServiceType().equalsIgnoreCase("VN") ||
		 * profileRequest.getServiceType().equalsIgnoreCase(
		 * AppConfig.config.getString("VN", "0100"))) { additionalInfoNoReply = new
		 * Additional_info(); additionalInfoNotReach = new Additional_info();
		 * additionalInfoNoReply.setName("CFNRY");
		 * additionalInfoNoReply.setValue("TRUE");
		 * additionalInfoNotReach.setName("CFNRC");
		 * additionalInfoNotReach.setValue("TRUE"); additional_info = new
		 * Additional_info[5]; additional_info[0] = additionalInfoDemandValue;
		 * additional_info[1] = additionalInfoNoReply; additional_info[2] =
		 * additionalInfoNotReach; additional_info[3] = additionalInfoUserId;
		 * additional_info[4] = additionalInfoRegionId;
		 * additional_information.setAdditional_info(additional_info);
		 * provisionRequestedServiceRequest
		 * .setAdditional_information(additional_information); } } else if
		 * (profileRequest.getMethodName().equalsIgnoreCase( "DeleteService")) {
		 * 
		 * if (profileRequest.getServiceType().equalsIgnoreCase("VM") ||
		 * profileRequest.getServiceType().equalsIgnoreCase(
		 * AppConfig.config.getString("VM", "0010")) ||
		 * profileRequest.getServiceType().equalsIgnoreCase(
		 * AppConfig.config.getString("notifyme", "notifyme"))) { additional_info = new
		 * Additional_info[2]; additional_info[0] = additionalInfoUserId;
		 * additional_info[1] = additionalInfoRegionId;
		 * additional_information.setAdditional_info(additional_info);
		 * provisionRequestedServiceRequest
		 * .setAdditional_information(additional_information); } if
		 * (profileRequest.getServiceType().equalsIgnoreCase("VN") ||
		 * profileRequest.getServiceType().equalsIgnoreCase(
		 * AppConfig.config.getString("VN", "0100"))) { additional_info = new
		 * Additional_info[2]; additional_info[0] = additionalInfoUserId;
		 * additional_info[1] = additionalInfoRegionId;
		 * additional_information.setAdditional_info(additional_info);
		 * provisionRequestedServiceRequest
		 * .setAdditional_information(additional_information);
		 * 
		 * }
		 * 
		 * additional_info = new Additional_info[3]; additional_info[0] =
		 * additionalInfoDemandValue; additional_info[1] = additionalInfoUserId;
		 * additional_info[2] = additionalInfoRegionId;
		 * additional_information.setAdditional_info(additional_info);
		 * provisionRequestedServiceRequest.setAdditional_information(
		 * additional_information);
		 * 
		 * } MessageBody messageBody = new MessageBody(); Request request = new
		 * Request(provisionRequestedServiceRequest); messageBody.setRequest(request);
		 * 
		 * MessageEnvelope messageEnvelope = new MessageEnvelope();
		 * messageEnvelope.setMessageBody(messageBody);
		 * 
		 * Response response = new Response();
		 * 
		 * org.apache.axis.client.Service service = new
		 * org.apache.axis.client.Service();
		 * AxisProperties.setProperty("axis.socketSecureFactory",
		 * "org.apache.axis.components.net.SunFakeTrustSocketFactory"); try { url = new
		 * URL(urlhit); logger.info("MSISDN[" + profileRequest.getMsisdn() +
		 * "] TransactionId[" + profileRequest.getTransactionId() +
		 * "] URL for CreateOrder[" + url + "]"); SelfCareServicePortTypeBindingStub
		 * bindingStub = new SelfCareServicePortTypeBindingStub( url, service);
		 * bindingStub.setUsername(AppConfig.config.getString( "tibco_username",
		 * "vcc")); bindingStub.setPassword(AppConfig.config.getString(
		 * "tibco_password", "vcc")); bindingStub.setTimeout(AppConfig.config
		 * .getInt("stub_timeout", 1000));
		 * 
		 * //crmRequest = gson.toJson(messageEnvelope); messageEnvelope = bindingStub
		 * .opProvisionRequestedService(messageEnvelope);
		 * 
		 * messageBody = messageEnvelope.getMessageBody();
		 * 
		 * response = messageBody.getResponse();
		 * 
		 * ProvisionRequestedServiceResponse provisionRequestedServiceResponse =
		 * response .getProvisionRequestedServiceResponse();
		 * 
		 * //crmResponse = gson.toJson(provisionRequestedServiceResponse); // Getting
		 * Response -
		 * 
		 * profileResponse.setTransactionId(provisionRequestedServiceResponse
		 * .getTransaction_id());
		 * profileResponse.setResponseCode(provisionRequestedServiceResponse
		 * .getResponse_code()); profileResponse
		 * .setResponseDescription(provisionRequestedServiceResponse
		 * .getResponse_description());
		 * profileResponse.setMethodName(provisionRequestedServiceResponse
		 * .getMethod_name());
		 * 
		 * if (profileResponse.getResponseCode().equalsIgnoreCase("done")) {
		 * profileResponse.setStatus("SUCCESS");
		 * logger.info("Response CreateOrder : TransactionId [" +
		 * profileResponse.getTransactionId() + "] MSISDN[" + profileRequest.getMsisdn()
		 * + "] Status [" + profileResponse.getStatus() + "] ResponseCode[" +
		 * profileResponse.getResponseCode() + "] ResponseDescription[" +
		 * profileResponse.getResponseDescription() + "]"); } else {
		 * profileResponse.setStatus("FAIL");
		 * logger.info("Response CreateOrder : TransactionId [" +
		 * profileResponse.getTransactionId() + "] MSISDN[" + profileRequest.getMsisdn()
		 * + "] Status [" + profileResponse.getStatus() + "] ResponseCode [" +
		 * profileResponse.getResponseCode() + "] ResponseDescription [" +
		 * profileResponse.getResponseDescription() + "]"); } } catch (AxisFault e) { if
		 * (e.getCause() instanceof java.net.SocketTimeoutException) { errorLogger
		 * .error("ErrorCode [" + AppConfig.config.getString( "errorcode_pattern",
		 * "VCC-CRMC-") + "90017] MSISDN[" + profileRequest.getMsisdn() +
		 * "] MethodName[" + profileRequest.getMethodName() + "] TransactionId [" +
		 * profileRequest.getTransactionId() + "] Language [" +
		 * profileRequest.getLanguage() + "] DemandValue [" +
		 * profileRequest.getDemandValue() +
		 * "][Socket ReadTime out Exception while sending CreateOrder request to Tibaco Interface] Error["
		 * + e.getMessage() + "]"); e.printStackTrace();
		 * profileResponse.setStatus("SUCCESS"); } else { errorLogger
		 * .error("ErrorCode [" + AppConfig.config.getString( "errorcode_pattern",
		 * "VCC-CRMC-") + "00003] MSISDN[" + profileRequest.getMsisdn() +
		 * "] MethodName[" + profileRequest.getMethodName() + "] TransactionId [" +
		 * profileRequest.getTransactionId() + "] Language [" +
		 * profileRequest.getLanguage() + "] DemandValue [" +
		 * profileRequest.getDemandValue() +
		 * "][AxisfaultException while sending CreateOrder request to Tibaco Interface] Error["
		 * + e.getMessage() + "]"); e.printStackTrace();
		 * profileResponse.setStatus("FAIL"); // profileResponse.setStatus("SUCCESS"); }
		 * } catch (Exception e) { errorLogger .error("ErrorCode [" +
		 * AppConfig.config.getString("errorcode_pattern", "VCC-CRMC-") +
		 * "00004] MSISDN[" + profileRequest.getMsisdn() + "] MethodName[" +
		 * profileRequest.getMethodName() + "] TransactionId [" +
		 * profileRequest.getTransactionId() + "] Language [" +
		 * profileRequest.getLanguage() + "] DemandValue [" +
		 * profileRequest.getDemandValue() +
		 * "] [Exception while sending CreateOrder request to Tibaco Interface] Error["
		 * + e.getMessage() + "]"); e.printStackTrace();
		 * profileResponse.setStatus("FAIL"); // profileResponse.setStatus("SUCCESS"); }
		 * 
		 * // crmLogger.info("{req:" + crmRequest + ",resp:" + crmResponse + "}");
		 */ }

	/*
	 * public void checkEligibility(ProfileRequest profileRequest, ProfileResponse
	 * profileResponse) {
	 * 
	 * 
	 * logger.info("CheckEligibility : TransactionID [" +
	 * profileRequest.getTransactionId() + "] MSISDN [" + profileRequest.getMsisdn()
	 * + "] ActionType [" + profileRequest.getActionType() + "] Language [" +
	 * profileRequest.getLanguage() + "]");
	 * 
	 * // String urlhit = //
	 * "http://www.tibco.com/Middleware/AutoServiceFulfilment/"; String urlhit =
	 * AppConfig.config.getString("autofulfilment_url",
	 * "https://mwsit.etisalat.ae:9157/Etisalat/Falcon/Tibco/IIT/DOM_CC/CustomerManagement/BS_SelfCareService"
	 * );
	 * 
	 * URL url = null;
	 * 
	 * logger.info("urlhit=============" + urlhit);
	 * 
	 * 
	 * CheckEligibilityRequest checkEligibilityRequest = new
	 * CheckEligibilityRequest();
	 * 
	 * ApplicationHeader applicationHeader = new ApplicationHeader(); //
	 * applicationHeader.setRequestedDate(profileRequest.getRequestedDate());
	 * applicationHeader.setRequestedSystem(profileRequest.getRequestedSystem());
	 * applicationHeader.setRetryLimit(profileRequest.getRetryLimit());
	 * applicationHeader.setTransactionID(profileRequest.getTransactionId());
	 * 
	 * CheckEligibilityRequestDataHeader dataHeader = new
	 * CheckEligibilityRequestDataHeader();
	 * 
	 * dataHeader.setChannel(profileRequest.getChannel());
	 * 
	 * dataHeader.setSubChannel(profileRequest.getSubChannel());
	 * dataHeader.setLanguage(profileRequest.getLanguage());
	 * dataHeader.setReceivedTime(profileRequest.getReceivedTime());
	 * 
	 * CheckEligibilityRequestDataHeaderOrderItem[] dataHeaderOrderArray = new
	 * CheckEligibilityRequestDataHeaderOrderItem[1];
	 * 
	 * CheckEligibilityRequestDataHeaderOrderItem dataHeaderOrderItem = new
	 * CheckEligibilityRequestDataHeaderOrderItem();
	 * dataHeaderOrderItem.setAccountNumber(profileRequest.getMsisdn());
	 * dataHeaderOrderItem.setActionType(profileRequest.getActionType());
	 * dataHeaderOrderItem.setParentOrderNumber(Integer.parseInt(profileRequest.
	 * getParentOrderNumber()));
	 * dataHeaderOrderItem.setOrderItemNumber(Integer.parseInt(profileRequest.
	 * getOrderItemNumber()));
	 * 
	 * dataHeaderOrderArray[0] = dataHeaderOrderItem;
	 * dataHeader.setOrderItem(dataHeaderOrderArray);
	 * 
	 * CheckEligibilityRequestDataHeaderOrderItemOfferID orderItemOfferID = new
	 * CheckEligibilityRequestDataHeaderOrderItemOfferID();
	 * orderItemOfferID.setName(profileRequest.getDemandId());
	 * orderItemOfferID.setValue(profileRequest.getDemandValue());
	 * 
	 * CheckEligibilityRequestDataHeaderOrderItemOfferID[]
	 * dataHeaderOrderItemOfferID = new
	 * CheckEligibilityRequestDataHeaderOrderItemOfferID[1];
	 * dataHeaderOrderItemOfferID[0] = orderItemOfferID;
	 * dataHeaderOrderItem.setOfferID(dataHeaderOrderItemOfferID);
	 * 
	 * CheckEligibilityRequestDataHeaderOrderItemOrderInformation orderInformation =
	 * new CheckEligibilityRequestDataHeaderOrderItemOrderInformation();
	 * orderInformation.setAttributeName("user_id");
	 * orderInformation.setAttributeValue(AppConfig.config.getString("user_id",
	 * "Channels"));
	 * 
	 * CheckEligibilityRequestDataHeaderOrderItemOrderInformation[]
	 * itemOrderInformations = new
	 * CheckEligibilityRequestDataHeaderOrderItemOrderInformation[1];
	 * itemOrderInformations[0] = orderInformation;
	 * dataHeaderOrderItem.setOrderInformation(itemOrderInformations);
	 * 
	 * checkEligibilityRequest.setApplicationHeader(applicationHeader);
	 * checkEligibilityRequest.setDataHeader(dataHeader);
	 * 
	 * org.apache.axis.client.Service service = new
	 * org.apache.axis.client.Service();
	 * 
	 * AxisProperties.setProperty("axis.socketSecureFactory",
	 * "org.apache.axis.components.net.SunFakeTrustSocketFactory");
	 * 
	 * try { logger.info("url=========="+url); url = new URL(urlhit);
	 * logger.info("MSISDN[" + profileRequest.getMsisdn() + "] TransactionId[" +
	 * profileRequest.getTransactionId() + "] URL for CheckEligibility[" + url +
	 * "]");
	 * 
	 * 
	 * SelfCareServicePortTypeBindingStub bindingStub = new
	 * SelfCareServicePortTypeBindingStub(url, service);
	 * bindingStub.setUsername(AppConfig.config.getString("tibco_username", "vcc"));
	 * bindingStub.setPassword(AppConfig.config.getString("tibco_password", "vcc"));
	 * bindingStub.setTimeout(AppConfig.config.getInt("stub_timeout", 1000));
	 * CheckEligibilityResponse checkEligibilityResponse =
	 * bindingStub.opCheckEligibility(checkEligibilityRequest);
	 * 
	 * // crmRequest = gson.toJson(checkEligibilityRequest); // crmResponse =
	 * gson.toJson(checkEligibilityResponse); // Getting Response -
	 * 
	 * profileResponse.setStatus("status");
	 * profileResponse.setResponseCode("ResponseCode"); // if
	 * (checkEligibilityResponse.getAckMessage().getErrorCode() != null) { if(true)
	 * { profileResponse.setErrorCode("ErrorCode");
	 * profileResponse.setErrorType("ErrorType");
	 * profileResponse.setErrorDescription("ErrorDescription"); }
	 * profileResponse.setAccountNumber( "AccountNumber");
	 * 
	 * 
	 * logger.info("profileResponse+++++++++++++++++++++"+profileResponse.
	 * getAccountNumber()+ "         "+profileResponse.getDemandId());
	 * 
	 * } catch (AxisFault e) { errorLogger.error("ErrorCode [" +
	 * AppConfig.config.getString("errorcode_pattern", "VCC-CRMC-") +
	 * "00003] MSISDN[" + profileRequest.getMsisdn() + "] MethodName[" +
	 * profileRequest.getMethodName() + "] TransactionId [" +
	 * profileRequest.getTransactionId() + "] Language [" +
	 * profileRequest.getLanguage() + "] DemandValue [" +
	 * profileRequest.getDemandValue() +
	 * "][Axisfault Exception while sending CheckEligibility request to Tibaco Interface] Error["
	 * + e.getMessage() + "] "); e.printStackTrace();
	 * profileResponse.setStatus("FAIL"); // profileResponse.setStatus("SUCCESS");
	 * 
	 * } catch (Exception e) { errorLogger.error("ErrorCode [" +
	 * AppConfig.config.getString("errorcode_pattern", "VCC-CRMC-") +
	 * "00005] MSISDN[" + profileRequest.getMsisdn() + "] MethodName[" +
	 * profileRequest.getMethodName() + "] TransactionId [" +
	 * profileRequest.getTransactionId() + "] Language [" +
	 * profileRequest.getLanguage() + "] DemandValue [" +
	 * profileRequest.getDemandValue() +
	 * "] [Exception while sending CheckEligibility request to Tibaco Interface] Error["
	 * + e.getMessage() + "] "); e.printStackTrace();
	 * profileResponse.setStatus("FAIL"); // profileResponse.setStatus("SUCCESS"); }
	 * // crmLogger.info("{req:" + crmRequest + ",resp:" + crmResponse + "}");
	 * 
	 * }
	 */
}
