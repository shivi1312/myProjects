package com.vcc.config;

import org.apache.log4j.Logger;

import com.vcc.config.AppConfig;

import edu.emory.mathcs.util.net.ConnectionPool;

public class TcpPool {
    final static Logger logger = Logger.getLogger(TcpPool.class);
    private TcpPool() {

    }

    
    private static ConnectionPool ruleEngineConPool = new ConnectionPool(
            AppConfig.config.getString("rule_engine_ip"),
            AppConfig.config.getInt("rule_engine_port"));
    
    public static ConnectionPool getRuleEngineConPool() {
        logger.info("the rule engine ip ["+AppConfig.config.getString("rule_engine_ip")+"] port ["+    AppConfig.config.getInt("rule_engine_port")+"]");
        //logger.info("Connection Pool :"+ruleEngineConPool);
        return ruleEngineConPool;
    }

    


}