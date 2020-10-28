package com.bkav.edoc.converter.impl;

import com.bkav.edoc.converter.Converter;
import com.bkav.edoc.converter.util.DBConnectionUtil;
import com.bkav.edoc.converter.util.StringQuery;
import com.bkav.edoc.converter.util.TokenUtil;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DynamicContactConverter implements Converter<EdocDynamicContact> {

    private static final Logger LOGGER = Logger.getLogger(DynamicContactConverter.class);

    @Override
    public List<EdocDynamicContact> getFromDatabase() throws SQLException {
        List<EdocDynamicContact> output = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.initPortalDBConnection()) {
            Statement stm;
            ResultSet rs = null;
            stm = connection.createStatement();
            rs = stm.executeQuery(StringQuery.GET_DYNAMIC_CONTACT);

            while (rs.next()) {
                EdocDynamicContact edocDynamicContact = new EdocDynamicContact();
                edocDynamicContact.setId(rs.getLong(1));
                edocDynamicContact.setName(rs.getString(2));
                edocDynamicContact.setInCharge(rs.getString(3));
                edocDynamicContact.setDomain(rs.getString(4));
                edocDynamicContact.setEmail(rs.getString(5));
                edocDynamicContact.setAddress(rs.getString(6));
                edocDynamicContact.setTelephone(rs.getString(7));
                edocDynamicContact.setFax(rs.getString(8));
                edocDynamicContact.setWebsite(rs.getString(9));
                edocDynamicContact.setType(rs.getString(10));
                edocDynamicContact.setVersion(rs.getString(11));
                edocDynamicContact.setToken("");
                output.add(edocDynamicContact);
            }
            return output;
        } catch (SQLException throwable) {
            LOGGER.error(throwable);
            return null;
        }
    }

    public static void main(String[] args) throws SQLException {
        DynamicContactConverter contactConverter = new DynamicContactConverter();
        List<EdocDynamicContact> output = new DynamicContactConverter().getFromDatabase();
        LOGGER.info("Preparing convert edoc dynamic contact with contact size " + output.size());
        contactConverter.convert(output);
        LOGGER.info("convert edoc dynamic contact done !!!! ");
    }

    @Override
    public void convert(List<EdocDynamicContact> list) throws SQLException {
        for (EdocDynamicContact contact : list) {
            String token = TokenUtil.getRandomNumber(contact.getDomain(), contact.getName());
            contact.setToken(token);
            contact.setStatus(true);
            EdocDynamicContactServiceUtil.createContact(contact);
        }
    }

}
