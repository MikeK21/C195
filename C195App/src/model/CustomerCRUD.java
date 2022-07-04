package model;

import helpers.JDBC;
import javafx.scene.control.Alert;
import utils.DataProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CustomerCRUD {

    public static void select() throws Exception {
        DataProvider.clearCustomers();
        PreparedStatement ps = JDBC.c.prepareStatement("SELECT * FROM CUSTOMERS");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            //System.out.println(rs.getInt(1));
            //System.out.println(rs.getString(2) + " " + rs.getString(3));
            Customer customer = new Customer(rs.getInt(1), rs.getString(4),rs.getString(3), rs.getString(5),rs.getString(2), rs.getInt(10));
            DataProvider.addCustomer(customer);
            //System.out.println(DataProvider.getAllCustomers());
        }
    }

    public static int insert( String Customer_Name, String Address, String Postal_Code, String Phone, String Created_By, String Last_Updated_By, int Division_ID) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By,Last_Update,Last_Updated_By,Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.c.prepareStatement(sql);
        ps.setString(1, Customer_Name);
        ps.setString(2,Address);
        ps.setString(3,Postal_Code);
        ps.setString(4,Phone);
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6,Created_By);
        ps.setString(7,LocalDateTime.now().toString());
        ps.setString(8,Last_Updated_By);
        ps.setInt(9,Division_ID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

    public static int update(String Customer_Name, String Address, String Postal_Code, String Phone, String Last_Updated_By, int Division_ID,  int Customer_ID) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.c.prepareStatement(sql);
        ps.setString(1, Customer_Name);
        ps.setString(2,Address);
        ps.setString(3,Postal_Code);
        ps.setString(4,Phone);
        ps.setString(5,Last_Updated_By);
        ps.setInt(6,Division_ID);
        ps.setInt(7,Customer_ID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int delete(int Customer_ID) throws SQLException {
        String sqlConstraint = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps1 = JDBC.c.prepareStatement(sqlConstraint);
        ps1.setInt(1,Customer_ID);
        ResultSet rowsAffectedConstraint = ps1.executeQuery();

        if (rowsAffectedConstraint.isBeforeFirst()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting Customers' Appointments");
            alert.setContentText("This will delete all Customers appointments as part of the delete action " +
                    "are you sure?");
            alert.showAndWait();
            String sqlConstraintDelete = "DELETE FROM appointments WHERE Customer_ID = ?";
            PreparedStatement ps2 = JDBC.c.prepareStatement(sqlConstraintDelete);
            ps2.setInt(1,Customer_ID);
            int rowsAffectedConstraintDelete = ps2.executeUpdate();
        }

        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps3 = JDBC.c.prepareStatement(sql);
        ps3.setInt(1,Customer_ID);
        int rowsAffected = ps3.executeUpdate();
        return rowsAffected;
    }

}
