/**
 * The FileBasedConfig class implements that writing request and
 * response logs.
 * @author Mayank Agrawal
 */

package com.vcc.config;

import org.apache.log4j.Logger;
import FileBaseLogging.FileLogWriter;

public class FileBasedConfig {
	
	final static Logger logger = Logger.getLogger(FileBasedConfig.class);
	public static FileLogWriter flWriter= new FileLogWriter();
	private FileBasedConfig(){
		
	}
	static{
		try{
			flWriter.setNewFileInterval(AppConfig.config.getInt("REQ_LOG_FILE_INTERVAL"));
			flWriter.setFilename(AppConfig.config.getString("Response_LOG_FILENAME"));
			flWriter.setFilePath(AppConfig.config.getString("REQ_LOG_FILEPATH"));
			flWriter.initialize();
		}
		catch(Exception ex)
		{
			logger.error("Exception in configure of FileBasedLog "+ex.getMessage());
		}
	}

}
