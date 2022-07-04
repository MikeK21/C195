package model;

import helpers.JDBC;
import javafx.scene.control.Alert;
import utils.DataProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AppointmentsCRUD {

    public static void select() throws Exception {
        DataProvider.clearAppointments();
        PreparedStatement ps = JDBC.c.prepareStatement("SELECT * FROM APPOINTMENTS");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getTimestamp(6), rs.getTimestamp(7), rs.getTimestamp(8), rs.getString(9), rs.getTimestamp(10), rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getInt(14));
            DataProvider.addAppointment(appointment);
        }
    }

    public static int insert(String Title, String Description, String Location, String Type, LocalDateTime Start, LocalDateTime End, String Created_By, String Last_Updated_By, int Customer_ID, int User_ID, int Contact_ID) throws Exception {
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (Title.isEmpty() || Description.isEmpty() || Location.isEmpty() ||
                Type.isEmpty() || Start.getDayOfWeek().toString().isEmpty() || End.getDayOfWeek().toString().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not all fields completed");
            alert.setContentText("Not all fields completed for Appointment Add");
            alert.showAndWait();
            return 0;
        }
        PreparedStatement ps = JDBC.c.prepareStatement(sql);
        ps.setString(1, Title);
        ps.setString(2,Description);
        ps.setString(3,Location);
        ps.setString(4,Type);
        ps.setTimestamp(5, Timestamp.valueOf(Start));
        ps.setTimestamp(6,Timestamp.valueOf(End));
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8,Created_By);
        ps.setString(9,LocalDateTime.now().toString());
        ps.setString(10,Last_Updated_By);
        ps.setInt(11, Customer_ID);
        ps.setInt(12, User_ID);
        ps.setInt(13, Contact_ID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int update(String Title, String Description, String Location, String Type, LocalDateTime Start, LocalDateTime End, String Last_Updated_By, int Customer_ID, int User_ID, int Contact_ID, int Appointment_ID) throws Exception {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        if (Title.isEmpty() || Description.isEmpty() || Location.isEmpty() ||
                Type.isEmpty() || Start.getDayOfWeek().toString().isEmpty() || End.getDayOfWeek().toString().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not all fields completed");
            alert.setContentText("Not all fields completed for Appointment Update");
            alert.showAndWait();
            return 0;
        }
        PreparedStatement ps = JDBC.c.prepareStatement(sql);
        ps.setString(1, Title);
        ps.setString(2,Description);
        ps.setString(3,Location);
        ps.setString(4,Type);
        ps.setTimestamp(5, Timestamp.valueOf(Start));
        ps.setTimestamp(6,Timestamp.valueOf(End));
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8,Last_Updated_By);
        ps.setInt(9, Customer_ID);
        ps.setInt(10, User_ID);
        ps.setInt(11, Contact_ID);
        ps.setInt(12,Appointment_ID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int deleteCustomerAppts(int Appointment_ID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.c.prepareStatement(sql);
        ps.setInt(1,Appointment_ID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

    public static int delete(int Appointment_ID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        if (String.valueOf(Appointment_ID).isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not all fields completed");
            alert.setContentText("No Appointment Selected for cancellation");
            alert.showAndWait();
            return 0;
        }
        PreparedStatement ps = JDBC.c.prepareStatement(sql);
        ps.setInt(1,Appointment_ID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

}
