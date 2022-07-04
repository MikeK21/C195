package controller;

import com.mysql.cj.protocol.Resultset;

import java.io.BufferedWriter;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import helpers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import utils.DataProvider;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.time.ZoneId;
import helpers.Zonify;

public class LoginScreen implements Initializable {
    public TextField usernameField;
    public PasswordField passwordField;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label timeLabel;
    public ZoneId zone;
    public Label welcomeMessage;
    public Button loginButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * Lambda 1: This Lambda takes a user language and creates an object from the Languify function
         * This makes it easier to evaluate new languages for scaling out to support other languages
         */
        Languify languified = language -> System.getProperty("user.language").trim().contains(language);
        Boolean systemLanguageFr = languified.getLocale("fr");
        if (systemLanguageFr) {
            welcomeMessage.setText("Accueillir");
            usernameLabel.setText("utilisateur");
            passwordLabel.setText("passe");        /**
             * Lambda 2: This Lambda takes a Zone and creates an object from the Zonify function
             * This makes it easier to evaluate new zones for scaling out to support other zones
             */
            Zonify zonified = zone -> ZoneId.of(zone);
            ZoneId systemZone = zonified.getDefZone("Europe/Paris");
            zone = systemZone;
        } else {
            welcomeMessage.setText("Welcome");
            usernameLabel.setText("Username");
            passwordLabel.setText("Password");
            Zonify zonified = zone -> ZoneId.of(zone);
            ZoneId systemZone = zonified.getDefZone("America/New_York");
            zone = systemZone;
        }

        timeLabel.setText("Zone: " + zone + " Region: " + System.getProperty("user.country"));


    }

    /**
     * This method logs login activity to loginactivity.txt
     * This method logs the following: login attempt, login success, login fail and otherwise logs as "unknown event"
     * @param eventType
     */
    public void logActivity(String eventType) {
        try {
            BufferedWriter loginActivity = new BufferedWriter(new FileWriter("loginactivity.txt", true));
            long epoch = System.currentTimeMillis() / 1000;
            Zonify zoneMaster = zone -> ZoneId.of(zone);
            ZoneId defaultZone = zoneMaster.getDefZone("America/New_York");
            ZonedDateTime zdt = ZonedDateTime.now(defaultZone);
            String userLoginAttempt;
            if (eventType.equals("loginAttempt")) {
                userLoginAttempt = usernameField.getText().trim().concat(" login attempted ")
                        .concat(zdt.toString())
                        .concat(" TIMESTAMP: ")
                        .concat(String.valueOf((epoch)));
            } else if (eventType.equals("loginFail")) {
                userLoginAttempt = usernameField.getText().trim().concat(" login failed ")
                        .concat(zdt.toString())
                        .concat(" TIMESTAMP: ")
                        .concat(String.valueOf((epoch)));
            } else if (eventType.equals("loginSuccess")) {
                userLoginAttempt = usernameField.getText().trim().concat(" login successful ")
                        .concat(zdt.toString())
                        .concat(" TIMESTAMP: ")
                        .concat(String.valueOf((epoch)));
            } else {
                userLoginAttempt = usernameField.getText().trim().concat("unknown log event: " + eventType)
                        .concat(zdt.toString())
                        .concat(" TIMESTAMP: ")
                        .concat(String.valueOf((epoch)));
            }
            loginActivity.append(userLoginAttempt);
            loginActivity.newLine();
            loginActivity.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method sends user and password data through after an attempted login to validate credentials
     * @param actionEvent
     * @throws Exception
     */
    public void clickLogin(ActionEvent actionEvent) throws Exception {
        System.out.println("Button clicked");;
        logActivity("loginAttempt");
        boolean userMatch = false;
        boolean passwordMatch = false;
        for (User user : DataProvider.getAllUsers()) {
            if (usernameField.getText().trim().equals(user.getUser()) && passwordField.getText().trim().equals(user.getPassword())) {
                System.out.println("USER IS CURRENTLY: ");
                System.out.println(user.getUser());
                DataProvider.setCurrentUser(user);
                userMatch = true;
                passwordMatch = true;
                break;
                }
            }
        if (userMatch == false || passwordMatch == false) {
            logActivity("loginFail");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setContentText("Invalid Credentials");
            System.out.println("Invalid Credentials");
            usernameField.setText("");
            passwordField.setText("");
            alert.showAndWait();
        } else {
            logActivity("loginSuccess");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setContentText("Login Successful");
            alert.showAndWait();
            Stage stage = null;
            start(stage, "/view/CustomerScreen.fxml");
        }
    }

    /**
     * This method loads a stage as specified
     * @param stage
     * @param resource
     * @throws Exception
     */
    public void start(Stage stage, String resource) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource(resource));
        stage = (Stage) loginButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
