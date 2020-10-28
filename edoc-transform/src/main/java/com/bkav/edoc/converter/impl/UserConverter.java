package com.bkav.edoc.converter.impl;

import com.bkav.edoc.converter.Converter;
import com.bkav.edoc.converter.util.DBConnectionUtil;
import com.bkav.edoc.converter.util.StringQuery;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.services.UserService;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserConverter implements Converter<User> {

    private static final Log log = LogFactory.getLog(UserConverter.class);
    private final static UserService USER_SERVICE = new UserService();

    @Override
    public List<User> getFromDatabase() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.initPortalDBConnection()) {
            Statement stm;
            ResultSet rs;
            stm = connection.createStatement();
            rs = stm.executeQuery(StringQuery.GET_USER);

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getLong(1));
                user.setUsername(rs.getString(2));
                user.setEmailAddress(rs.getString(3).trim());
                user.setDisplayName(rs.getString(4).trim());
                user.setPassword(rs.getString(5).trim());
                user.setStatus(rs.getBoolean(6));
                user.setCreateDate(rs.getDate(7));
                user.setModifiedDate(rs.getDate(8));
                user.setLastLoginDate(rs.getDate(9));
                user.setLastLoginIP(rs.getString(10));
                users.add(user);
            }
            return users;
        } catch (SQLException throwable) {
            log.error(throwable);
            return null;
        }
    }

    @Override
    public void convert(List<User> list) throws SQLException {
        for (User user : list) {
            EdocDynamicContact contact = getContactFromUserId(user.getUserId());
            user.setDynamicContact(contact);
            USER_SERVICE.createUser(user);
        }
    }

    private EdocDynamicContact getContactFromUserId(long userId) throws SQLException {
        long organFromOldDb = getFromDatabase(userId);
        String organDomain = getDomain(organFromOldDb);
        if (!organDomain.equals("")) {
            return EdocDynamicContactServiceUtil.findContactByDomain(organDomain);
        }
        return null;
    }

    private long getFromDatabase(long userId) throws SQLException {
        long result = -1;
        try (Connection connection = DBConnectionUtil.initPortalDBConnection()) {
            Statement stm = null;
            ResultSet rs = null;
            PreparedStatement preSt = connection.prepareStatement(StringQuery.GET_USER_ORGANIZATION_BY_USERID);
            preSt.setLong(1, userId);
            rs = preSt.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            }

        } catch (SQLException throwable) {
            log.error(throwable);
        }
        return result;
    }

    private String getDomain(long organOldID) {
        String result = "";
        try (Connection connection = DBConnectionUtil.initPortalDBConnection()) {
            Statement stm = null;
            ResultSet rs = null;
            PreparedStatement preSt = connection.prepareStatement(StringQuery.GET_DYNAMIC_CONTACT_BY_ORG_ID);
            preSt.setLong(1, organOldID);
            rs = preSt.executeQuery();
            if (rs.next()) {
                result = rs.getString(5);
            }

        } catch (SQLException throwable) {
            log.error(throwable);
        }
        return result;
    }


    public static void main(String[] args) throws SQLException {
        UserConverter userConverter = new UserConverter();
        List<User> users = userConverter.getFromDatabase();
        userConverter.convert(users);
        System.out.println("Done !!!!!!!!!!!");
    }

}
