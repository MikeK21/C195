package main;

import helpers.JDBC;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import model.*;
import helpers.Zonify;

import java.sql.Time;
import java.time.ZoneId;
import java.util.TimeZone;

public class Main extends Application {
    /**
     * Loads login scene and accepts a stage object to load the target scene.
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Main function to instantiate methods needing to be called at intial runtime, such as CRUD related operations.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Zonify zonified = zone -> ZoneId.of(zone);
        ZoneId systemZone = zonified.getDefZone("America/New_York");
        System.out.println("systemZoneLambda: ");
        System.out.println("Region: " + System.getProperty("user.country"));
        System.out.println(systemZone);

        JDBC.openConnection();
        UserCRUD.select();
        CustomerCRUD.select();
        AppointmentsCRUD.select();
        DivisionCRUD.select();
        CountriesCRUD.select();
        ContactCRUD.select();
        ReportsCRUD.selectTodays();
        launch(args);

        JDBC.closeConnection();


    }
}
