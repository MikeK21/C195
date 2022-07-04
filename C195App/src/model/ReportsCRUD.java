package model;

import helpers.JDBC;
import utils.DataProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;

public class ReportsCRUD {

    public static void selectTodays() throws Exception {
        DataProvider.clearTodaysAppointments();
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime convertStart = today.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime convertEnd = today.withHour(23).withMinute(59).withSecond(59);
        //System.out.println("Start: " + String.valueOf(Timestamp.valueOf(convertStart)));
        //System.out.println("End: " + String.valueOf(Timestamp.valueOf(convertEnd)));


        PreparedStatement ps = JDBC.c.prepareStatement("SELECT * FROM APPOINTMENTS WHERE start>='"
        .concat(String.valueOf(Timestamp.valueOf(convertStart)))+"' and end<='".concat(String.valueOf(Timestamp.valueOf(convertEnd))).concat("'"));
        //DBQuery.setStatement(JDBC.c, "SELECT * FROM USERS");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getTimestamp(6), rs.getTimestamp(7), rs.getTimestamp(8), rs.getString(9), rs.getTimestamp(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getInt(14));
            DataProvider.addTodaysAppointment(appointment);
        }
    }

    public static void selectTypeMonth(String type, String stringMonth) throws Exception {
        DataProvider.clearTypeMonthAppointments();

        PreparedStatement ps = JDBC.c.prepareStatement("SELECT * FROM APPOINTMENTS WHERE monthname(start) IN('"
                .concat(stringMonth)+"') and type IN ('".concat(type).concat("')"));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getTimestamp(6), rs.getTimestamp(7), rs.getTimestamp(8), rs.getString(9), rs.getTimestamp(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getInt(14));
            DataProvider.addTypeMonthAppointment(appointment);
        }
    }

    public static void selectContact(String contactName) throws Exception {
        DataProvider.clearContactAppointments();
        // Get the contacts hashmap to pull contact ID from contact name
        HashMap<String, Integer> contactMap = DataProvider.getContactMap();
        Integer contactID = contactMap.get(contactName);

        PreparedStatement ps = JDBC.c.prepareStatement("SELECT * FROM APPOINTMENTS WHERE Contact_ID IN("
                .concat(contactID.toString())+")");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getTimestamp(6), rs.getTimestamp(7), rs.getTimestamp(8), rs.getString(9), rs.getTimestamp(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getInt(14));
            DataProvider.addContactAppointment(appointment);
        }
    }
}
