package com.vcc.controller;

import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.vcc.handler.ProfileHandler;
import com.vcc.request.ProfileRequest;
import com.vcc.response.ProfileResponse;

@RestController
@RequestMapping("/")
public class VccCrmController {

	final static Logger logger = Logger.getLogger(VccCrmController.class);
	final static Logger ruleLogger = Logger.getLogger("ruleLogger");
	private static Gson gson = new Gson();

	@RequestMapping(value = "create.profile", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	ProfileResponse createProfile(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {
		logger.info("create.profile request: "+gson.toJson(profileRequest));
		profileRequest.setReqType("create");
		ProfileHandler profileHandler = new ProfileHandler();
		profileHandler.createProfile(profileRequest, bindingResult,
				profileResponse);
		profileHandler = null;
		logger.info("create.profile response :- "+ gson.toJson(profileResponse));
		
		ruleLogger.info("{req:"+gson.toJson(profileRequest)+",resp:"+gson.toJson(profileResponse)+"}");
		
		return profileResponse;
	}
	
	@RequestMapping(value = "modify.profile", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	ProfileResponse modifyProfile(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		logger.info("modify.profile request: "+gson.toJson(profileRequest));
		profileRequest.setReqType("modify");
		ProfileHandler profileHandler = new ProfileHandler();
		profileHandler.modifyProfile(profileRequest, bindingResult,
				profileResponse);
		profileHandler = null;
		logger.info("modify.profile response :- "+ gson.toJson(profileResponse));
		
		ruleLogger.info("{req:"+gson.toJson(profileRequest)+",resp:"+gson.toJson(profileResponse)+"}");
		
		return profileResponse;
	}

	@RequestMapping(value = "check.eligibility", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	ProfileResponse checkEligibility(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		logger.info("check.eligibility request: "+gson.toJson(profileRequest));
		profileRequest.setReqType("check");
		ProfileHandler profileHandler = new ProfileHandler();
		profileHandler.checkEligibility(profileRequest, bindingResult,
				profileResponse);
		profileHandler = null;
		logger.info("check.eligibility response :- "
				+ gson.toJson(profileResponse));
		
		ruleLogger.info("{req:"+gson.toJson(profileRequest)+",resp:"+gson.toJson(profileResponse)+"}");
		return profileResponse;
	}

	@RequestMapping(value = "get.subtype", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	ProfileResponse queryHLR(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		logger.info("get.subtype request: "+gson.toJson(profileRequest));
		ProfileHandler profileHandler = new ProfileHandler();
		profileHandler.getSubType(profileRequest, bindingResult,
				profileResponse);
		profileHandler = null;
		logger.info("get.subtype response :- " + gson.toJson(profileResponse));
		
		ruleLogger.info("{req:"+gson.toJson(profileRequest)+",resp:"+gson.toJson(profileResponse)+"}");
		return profileResponse;
	}

	@RequestMapping(value = "get.AccountDetails", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	ProfileResponse getAccountDetails(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		logger.info("get.AccountDetails request: "+gson.toJson(profileRequest));
		ProfileHandler profileHandler = new ProfileHandler();
		profileHandler.getAccountDetails(profileRequest, bindingResult,
				profileResponse);
		profileHandler = null;
		logger.info("get.AccountDetails response :- "+ gson.toJson(profileResponse));
		
		ruleLogger.info("{req:"+gson.toJson(profileRequest)+",resp:"+gson.toJson(profileResponse)+"}");
		return profileResponse;
	}

	@RequestMapping(value = "get.ActiveTrigger", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	ProfileResponse getActiveTrigger(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {

		logger.info("get.ActiveTrigger request: "+gson.toJson(profileRequest));
		ProfileHandler profileHandler = new ProfileHandler();
		profileHandler.getActiveTrigger(profileRequest, bindingResult,
				profileResponse);
		profileHandler = null;
		logger.info("get.ActiveTrigger response :- "+ gson.toJson(profileResponse));
		
		ruleLogger.info("{req:"+gson.toJson(profileRequest)+",resp:"+gson.toJson(profileResponse)+"}");
		return profileResponse;
	}

	@RequestMapping(value = "create.order", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	ProfileResponse createOrder(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {
		logger.info("create.order request: "+gson.toJson(profileRequest));
		ProfileHandler profileHandler = new ProfileHandler();
		profileHandler.createOrder(profileRequest, bindingResult,
				profileResponse);
		profileHandler = null;
		logger.info("create.order response :- " + gson.toJson(profileResponse));
		
		ruleLogger.info("{req:"+gson.toJson(profileRequest)+",resp:"+gson.toJson(profileResponse)+"}");
		return profileResponse;
	}

	@RequestMapping(value = "update.profile", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	ProfileResponse updateProfile(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {
		logger.info("update.profile request: "+gson.toJson(profileRequest));
		ProfileHandler profileHandler = new ProfileHandler();
		profileHandler.updateUserProfile(profileRequest, bindingResult,
				profileResponse);
		profileHandler = null;
		logger.info("update.profile response :- "+ gson.toJson(profileResponse));
		
		ruleLogger.info("{req:"+gson.toJson(profileRequest)+",resp:"+gson.toJson(profileResponse)+"}");
		return profileResponse;
	}
	@RequestMapping(value = "notifyme.deact", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	ProfileResponse notifyMeOptOut(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {
		logger.info("notifyme.deact request: "+gson.toJson(profileRequest));
		ProfileHandler profileHandler = new ProfileHandler();
		profileHandler.notifymeOptOut(profileRequest, bindingResult,
				profileResponse);
		profileHandler = null;
		logger.info("notifyme.deact response :-  "+ gson.toJson(profileResponse));
		
		return profileResponse;
	}
	
	
	@RequestMapping(value = "notifyme.act", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	ProfileResponse notifyMeOptIn(ProfileRequest profileRequest,
			BindingResult bindingResult, ProfileResponse profileResponse) {
		logger.info("notifyme.act request: "+gson.toJson(profileRequest));
		ProfileHandler profileHandler = new ProfileHandler();
		profileHandler.notifymeOptIn(profileRequest, bindingResult,
				profileResponse);
		profileHandler = null;
		logger.info("notifyme.act response :-  "+ gson.toJson(profileResponse));
		
		return profileResponse;
	}
	
	
}