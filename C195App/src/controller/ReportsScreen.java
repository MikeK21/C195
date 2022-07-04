package controller;

import helpers.Languify;
import helpers.Zonify;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.ReportsCRUD;
import model.User;
import utils.DataProvider;

import javax.xml.crypto.Data;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

/**
 * This is the reports screen that gives you the option to configure and select the specialized reports made available.
 */
public class ReportsScreen implements Initializable {

    public Button todayApptButton;
    public ComboBox typeComboBox;
    public ComboBox monthComboBox;
    public Button typeMonthGenerateButton;
    public Button generateContactButton;
    public ComboBox contactComboBox;
    String[] monthNames = new DateFormatSymbols().getMonths();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initialize the type combo box
        for ( Appointment appointment : DataProvider.getAllAppointments()) {
            if (!typeComboBox.getItems().contains(appointment.getType())) {
                typeComboBox.getItems().addAll(
                        appointment.getType()
                );
            }
        }
        // Initialize the Contact combo box
        for ( Contact contact : DataProvider.getAllContacts()) {
            if (!contactComboBox.getItems().contains(contact.getContact_Name())) {
                contactComboBox.getItems().addAll(
                        contact.getContact_Name()
                );
            }
        }


        for (String month : monthNames) {
            if (!month.isEmpty()) {
                monthComboBox.getItems().addAll(
                        month
                );
            }
        }

    }

    /**
     * This button will load the "todays report" screen that shows all of the appointments for the current day.
     * @param actionEvent
     * @throws Exception
     */
    public void clickTodayAppt(ActionEvent actionEvent) throws Exception {
        Stage stage = null;
        start(stage, "/view/TodaysReportScreen.fxml");
    }

    public void start(Stage stage, String fxmlTarget) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlTarget));
        stage = (Stage) todayApptButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This will generate the type month details needed to pass to the Database method to filter appointments for report.
     * @param actionEvent
     * @throws Exception
     */
    public void generateTypeMonth(ActionEvent actionEvent) throws Exception {
        try {
            ReportsCRUD.selectTypeMonth(typeComboBox.getSelectionModel().getSelectedItem().toString(), monthComboBox.getSelectionModel().getSelectedItem().toString());
            Stage stage = null;
            start(stage, "/view/MonthTypeScreen.fxml");
        }
        catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Type and/or Month Combo Box not selected");
            alert.setContentText("Please select a month and type");
            alert.show();
        }
    }

    public void generateContact(ActionEvent actionEvent) {
        try {
            ReportsCRUD.selectContact(contactComboBox.getSelectionModel().getSelectedItem().toString());
            Stage stage = null;
            start(stage, "/view/ContactsScreen.fxml");
        }
        catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Contacts Combo Box not selected");
            alert.setContentText("Please select a Contact");
            alert.show();
        }

    }

    /**
     * This button will take users back to the customer screen.
     * @param actionEvent
     * @throws Exception
     */
    public void clickBackButton(ActionEvent actionEvent) throws Exception {
        Stage stage = null;
        start(stage, "/view/CustomerScreen.fxml");
    }
}
