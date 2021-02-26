package com.bkav.edoc.converter.util;

/**
 * @author QuangCV
 */
public class DBToolConfigParams {

	/**
	 * Portal Database config parameters
	 * @reference configuration.properties
	 */
	public static final String PORTAL_DB_DRIVER_CLASSNAME = "portal.db.driverClassName";
	public static final String PORTAL_DB_CONNECTION_URL = "portal.db.connectionURL";
	public static final String PORTAL_DB_USERNAME = "portal.db.username";
	public static final String PORTAL_DB_PASSWORD = "portal.db.password";
	
	/**
	 * Convert Database config parameters
	 * @reference configuration.properties
	 */
	public static final String CONVERT_DB_DRIVER_CLASSNAME = "convert.db.driverClassName";
	public static final String CONVERT_DB_CONNECTION_URL = "convert.db.connectionURL";
	public static final String CONVERT_DB_USERNAME = "convert.db.username";
	public static final String CONVERT_DB_PASSWORD = "convert.db.password";
	
	
	/**
	 * COnverted article small image folder path
	 * @reference configuration.properties
	 */
	public static final String ARTICLE_IMAGE_FOLDER_PATH = "converted.article.image.folder.path";
	
	public static final String VBPQ_ATTACHMENT_FOLDER_PATH = "converted.vbpq.attachment.folder.path";
	
	public static final String TTHC_ATTACHMENT_FOLDER_PATH = "converted.tthc.attachment.folder.path";
}

