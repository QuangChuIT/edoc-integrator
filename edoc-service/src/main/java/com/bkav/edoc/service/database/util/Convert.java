package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;

import java.sql.*;

public class Convert {
    public static void main(String[] args) {
        // JDBC driver name and database URL
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/edoc_lamdong_test?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";

        //  Database credentials
        String USER = "root";
        String PASS = "123abc@A";
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT name, domain, email,token FROM edoc_dynamiccontact";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                String name = rs.getString("name");
                String domain = rs.getString("domain");
                String email = rs.getString("email");
                String token = rs.getString("token");
                if (email == null) {
                    email = "";
                }
                EdocDynamicContact edocDynamicContact = EdocDynamicContactServiceUtil.findContactByDomain(domain);
                if (edocDynamicContact == null) {
                    System.out.println("-------------------------- insert to database -----------------------------" + domain + "-" + name);
                    EdocDynamicContact contact = new EdocDynamicContact();
                    contact.setName(name);
                    contact.setEmail(email);
                    contact.setDomain(domain);
                    contact.setToken(token);
                    EdocDynamicContactServiceUtil.createContact(contact);
                } else {
                    System.out.println("-------------------------- contact exist in database -----------------------------" + domain + "-" + name);
                }
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }//Handle errors for Class.forName
        finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException ignored) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }
}
