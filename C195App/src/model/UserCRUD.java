package model;

import helpers.JDBC;
import utils.DataProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserCRUD {

    public static void select() throws Exception {

        PreparedStatement ps = JDBC.c.prepareStatement("SELECT * FROM USERS");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = new User(rs.getInt(1), rs.getString(2), rs.getString(3));
            DataProvider.addUser(user);
        }

    }

}
