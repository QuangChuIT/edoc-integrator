package com.bkav.edoc.converter.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DBConnectionUtil {


	public static Connection initPortalDBConnection(){
		
		Connection portalDBConnection = null;
		
		try {
			
			String portalDBDriverClassName = PropsUtil.get(DBToolConfigParams.PORTAL_DB_DRIVER_CLASSNAME);
			
			String portalDBConnectionURL = PropsUtil.get(DBToolConfigParams.PORTAL_DB_CONNECTION_URL);
			
			String portalDBUser =PropsUtil.get(DBToolConfigParams.PORTAL_DB_USERNAME);
			
			String portalDBPassword = PropsUtil.get(DBToolConfigParams.PORTAL_DB_PASSWORD);
			
			if(Validator.isNotNull(portalDBDriverClassName) && Validator.isNotNull(portalDBConnectionURL) && Validator.isNotNull(portalDBUser)){
				
				_log.info(DBToolConstants.PORTAL_DB_CONNECTING_MSG);
				
				portalDBConnection = makeDBConnection(portalDBDriverClassName, portalDBConnectionURL, portalDBUser, portalDBPassword);
				
				_log.info(DBToolConstants.PORTAL_DB_CONNECTION_SUCCESS_MSG);
			}else{
				
				_log.info(DBToolConstants.PORTAL_DB_INVALID_CONFIG_PARAM);
				
			}

		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return portalDBConnection;
	}
	

	public static Connection initConvertDBConnection(){
		
		Connection portalDBConnection = null;
		
		try {
			String convertDBDriverClassName = PropsUtil.get(DBToolConfigParams.CONVERT_DB_DRIVER_CLASSNAME);
			
			String convertDBConnectionURL = PropsUtil.get(DBToolConfigParams.CONVERT_DB_CONNECTION_URL);
			
			String convertDBUser = PropsUtil.get(DBToolConfigParams.CONVERT_DB_USERNAME);
			
			String convertDBPassword = PropsUtil.get(DBToolConfigParams.CONVERT_DB_PASSWORD);
			
			if(Validator.isNotNull(convertDBDriverClassName) && Validator.isNotNull(convertDBConnectionURL) && Validator.isNotNull(convertDBUser)){
				
				_log.info(DBToolConstants.CONVERT_DB_CONNECTING_MSG);
				
				portalDBConnection = makeDBConnection(convertDBDriverClassName, convertDBConnectionURL, convertDBUser, convertDBPassword);
				
				_log.info(DBToolConstants.CONVERT_DB_CONNECTION_SUCCESS_MSG);
			
			}else{
				
				_log.info(DBToolConstants.CONVERT_DB_INVALID_CONFIG_PARAM);
				
			}

		} catch ( ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return portalDBConnection;
	}
	
	public static void closeConnection(Connection connection){
		
		if(connection != null){
			
			try {
				
				connection.close();
				
			} catch (SQLException e) {
				_log.error(e);
			}
		}
	}
	
	protected static Connection makeDBConnection(String dbDriverClassName, String dbConnectionURL, String dbUserName, String dbPassword)
			throws ClassNotFoundException, SQLException {

		Class.forName(dbDriverClassName);

		return DriverManager.getConnection(dbConnectionURL, dbUserName, dbPassword);
	}

	public static void main(String[] args) {
		Connection connection = initPortalDBConnection();
		if(connection != null){
			System.out.println("success !!!");
		}
	}
	private static final Logger	_log	= Logger.getLogger(DBConnectionUtil.class.getName());
}
