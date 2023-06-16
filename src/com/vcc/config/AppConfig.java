package com.vcc.config;

import java.io.File;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vcc.domain.VccGmatMsgStore;
import com.vcc.domain.VccLbsTemplates;
import com.vcc.model.AppConfigParamRowMapper;
import com.vcc.model.AppConfigParam;

public class AppConfig {
	private final static Logger logger = Logger.getLogger(AppConfig.class);
	final static Logger errorLogger = Logger.getLogger("errorLogger");

	private AppConfig(){	
	}
	
	public static CombinedConfiguration config=null;
	
	@Autowired
	private AppConfig(DataSource dataSource) {
		config.setProperty("datasource", dataSource);
	}
	static {
		try {
			DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
			builder.setFile(new File("config.xml"));
			config = builder.getConfiguration(true);
		}catch(NullPointerException npe){
			errorLogger.error("ErrorCode ["
						+ AppConfig.config.getString("errorcode_pattern",
								"VCC-CRMC-") + "90003] [Null Pointer Exception in reading config File] Error[ "+npe.getMessage()+"]");
		}
		catch (Exception e) {
			errorLogger.error("ErrorCode ["
						+ AppConfig.config.getString("errorcode_pattern",
								"VCC-CRMC-") + "00001] [Exception in reading config File] Error[ "+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
	
	
}
