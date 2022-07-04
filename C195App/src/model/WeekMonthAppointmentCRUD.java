package model;

import com.sun.javafx.charts.Legend;
import helpers.JDBC;
import utils.DataProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;

public class WeekMonthAppointmentCRUD {

    public static void selectMonth(Integer customerID, String stringMonth) throws Exception {

        PreparedStatement ps = JDBC.c.prepareStatement("SELECT * FROM APPOINTMENTS WHERE monthname(start) IN('"
                .concat(stringMonth) + "') and Customer_ID IN (".concat(customerID.toString().concat(")")));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getTimestamp(6), rs.getTimestamp(7), rs.getTimestamp(8), rs.getString(9), rs.getTimestamp(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getInt(14));
            DataProvider.addMonthsAppointment(appointment);
        }
    }    public static void selectWeek(Integer customerID) throws Exception {

        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endWeekDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        //DayOfWeek startWeek = today.getDayOfWeek();
        DayOfWeek endWeek = DayOfWeek.valueOf("SUNDAY");
        // determine last day of current week
        LocalDateTime endOfWeek = endWeekDay.with(TemporalAdjusters.nextOrSame(endWeek));


        PreparedStatement ps = JDBC.c.prepareStatement("SELECT * FROM APPOINTMENTS WHERE Customer_ID IN (".concat(String.valueOf(customerID)) + ") and start>='"
                .concat(String.valueOf(Timestamp.valueOf(today)))+"' and end<='".concat(String.valueOf(Timestamp.valueOf(endOfWeek))).concat("'"));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getTimestamp(6), rs.getTimestamp(7), rs.getTimestamp(8), rs.getString(9), rs.getTimestamp(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getInt(14));
            DataProvider.addWeeksAppointment(appointment);
            }
    }
}
