package model;

import helpers.JDBC;
import utils.DataProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DivisionCRUD {

    public static void select() throws Exception {
        PreparedStatement ps = JDBC.c.prepareStatement("SELECT * FROM first_level_divisions");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            //System.out.println(rs.getInt(1));
            //System.out.println(rs.getString(2) + " " + rs.getString(3));
            Division division = new Division(rs.getInt(1),rs.getString(2),rs.getDate(3),rs.getString(4),rs.getTimestamp(5),rs.getString(6), rs.getInt(7));
            DataProvider.addDivision(division);
            //System.out.println(DataProvider.getAllDivisions());
        }
    }
/*
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
 */

}
