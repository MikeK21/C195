package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import utils.DataProvider;

import javax.xml.crypto.Data;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The CustomerScreen class contains controller methods for navigating the Customer Screen.
 * A user is presented the customer screen after a successful login.
 * This controller provides functionality to select a customer to make appointment modifications
 */
public class CustomerScreen implements Initializable {
    public TextField phoneNumberField;
    public TextField postalCodeField;
    public TextField customerNameField;
    public TextField addressField;
    public ComboBox divisionComboBox;
    public ComboBox countryComboBox;
    public TextField customerIDField;
    public Button updateCustomerButton;
    public TextField upCustomerName;
    public TextField upCustomerAddress;
    public TextField upCustomerPostal;
    public TextField upCustomerPhone;
    public ComboBox upCountryComboBox;
    public ComboBox upDivisionComboBox;
    public Button upCustomerButton;
    public Button delCustomerButton;
    public TextField upCustomerID;
    public Button addCustomerButton;
    public Button selectButton;
    public Button apptButton;
    public Button reportsButton;
    @FXML
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    @FXML
    private TableView<Customer> customerTable;
    // Initialize the columns
    @FXML
    private TableColumn<?, ?> customerID;
    @FXML
    private TableColumn<?, ?> customerName;
    @FXML
    private TableColumn<?, ?> customerPhone;
    @FXML
    private TableColumn<?, ?> customerAddress;
    @FXML
    private TableColumn<?, ?> customerPostalCode;
    @FXML
    private TableColumn<?, ?> customerDivID;

    int apptStatusCounter = 0;

    /**
     * This method is the initialization method for updates to Customer Screen resources.
     * @param url
     * @param resourceBundle
     */

    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        if (apptStatusCounter < 1) {
            DataProvider.checkForAppt();
            apptStatusCounter++;
        }
 */

        customerID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerDivID.setCellValueFactory(new PropertyValueFactory<>("Division_ID"));

        customerTable.setItems(customerList);

        for (Country country : DataProvider.getAllCountries()){
            countryComboBox.getItems().addAll(
                    country.getCountry()
            );
        }

        for (Division division : DataProvider.getAllDivisions()) {
                divisionComboBox.getItems().addAll(
                        division.getDivision()
                );
            }
        Country currentCountry = (Country) countryComboBox.getSelectionModel().getSelectedItem();
        DataProvider.setCurrentCountry(currentCountry);

    }

    /**
     * This method accepts an Action Event when the select country combobox is interacted with.
     * @param actionEvent
     */
    public void selectCountry(ActionEvent actionEvent) {
        divisionComboBox.getItems().clear();
        String currentCountry = countryComboBox.getValue().toString();
        DataProvider.setFocusDivision(currentCountry);
        divisionComboBox.getItems().removeAll();
        for (Division d : DataProvider.getFocusDivision()) {
            divisionComboBox.getItems().addAll(
                    d.getDivision()
            );
        }
    }

    /**
     * this method is used to take the country id and incorporate a filtered list of linked divisions based on country
     * @param comboBox
     * @return
     */
    public int determineDivID(ComboBox comboBox) {
        int focusDivisionID = 0;
        for (Division division : DataProvider.getAllDivisions()) {
            if (comboBox.getValue().toString() == division.getDivision()) {
                focusDivisionID = division.getDivision_ID();
            }
        }
        if (focusDivisionID == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Division ID");
            alert.setContentText("Could not determine division ID");
            alert.show();
        }
        return focusDivisionID;
    }

    /**
     * This method accepts an Action Event for when the Add button is selected.
     * This method makes a call to the CustomersCRUD
     * @param actionEvent
     * @throws SQLException
     */
    public void clickAddCustomer(ActionEvent actionEvent) throws SQLException {

        int focusDivisionID = determineDivID(divisionComboBox);
        if (customerNameField.getText().isEmpty() || addressField.getText().isEmpty() ||
                postalCodeField.getText().isEmpty() || postalCodeField.getText().isEmpty() ||
                phoneNumberField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not all fields completed");
            alert.setContentText("Not all fields completed for Customer Add");
            alert.show();
        }
        try {
            CustomerCRUD.insert(customerNameField.getText().trim(), addressField.getText().trim(),postalCodeField.getText().trim(), phoneNumberField.getText().trim(), DataProvider.getCurrentUser().get(0).getUser(),  DataProvider.getCurrentUser().get(0).getUser(), focusDivisionID);
            refreshTable();
        }
        catch(Exception e) {
            System.out.println("Error Trying to insert into the database");
            System.out.println(e);
        }
    }


    /**
     * Refreshes local Customer Table from the initalize function to keep it up to date.
     * @throws Exception
     */
    public void refreshTable() throws Exception {
        customerList.clear();
        CustomerCRUD.select();

        for (Customer customer : DataProvider.getAllCustomers()) {
            customerList.add(customer);
        }
        customerTable.refresh();
        customerTable.setItems(customerList);
        DataProvider.checkForAppt();
    }

    /**
     * This method updates the customer that is selected for modification.
     * @param actionEvent
     */
    public void clickUpCustomer(ActionEvent actionEvent) {
        int focusDivisionID = determineDivID(upDivisionComboBox);
        if (upCustomerName.getText().isEmpty() || upCustomerAddress.getText().isEmpty() ||
                upCustomerPostal.getText().isEmpty() || upCustomerPostal.getText().isEmpty() ||
                upCustomerPhone.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not all fields completed");
            alert.setContentText("Not all fields completed for Customer Update");
            alert.show();
        }
        try {
            CustomerCRUD.update(upCustomerName.getText(),upCustomerAddress.getText(),upCustomerPostal.getText(),upCustomerPhone.getText(),DataProvider.getCurrentUser().get(0).getUser(),focusDivisionID,Integer.parseInt(upCustomerID.getText()));
            refreshTable();
        }
        catch(Exception e) {
            System.out.println("Error Trying to insert into the database");
            System.out.println(e);
        }
    }

    /**
     * This method deletese a customer that is selected.
     * @param actionEvent
     * @throws Exception
     */
    public void clickDelCustomer(ActionEvent actionEvent) throws Exception {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setContentText("Are you sure you'd like to delete the selected record?");
        alert.showAndWait();
        AppointmentsCRUD.deleteCustomerAppts(Integer.parseInt(upCustomerID.getText()));
        CustomerCRUD.delete(Integer.parseInt(upCustomerID.getText()));
        refreshTable();
    }

    /**
     * This method takes in an Action Event for when select is clicked and pulls down customer information to relevant fields.
     * @param actionEvent
     */
    public void clickSelectButton(ActionEvent actionEvent) {

        upDivisionComboBox.getItems().clear();
        upCountryComboBox.getItems().clear();
        if (customerTable.getSelectionModel().getSelectedItem().getCustomer_ID() > 0) {
            Customer focusCustomer = customerTable.getSelectionModel().getSelectedItem();
            upCustomerID.setText(String.valueOf(focusCustomer.getCustomer_ID()));
            upCustomerName.setText(focusCustomer.getCustomer_Name());
            upCustomerAddress.setText(focusCustomer.getAddress());
            upCustomerPhone.setText(focusCustomer.getPhoneNumber());
            upCustomerPostal.setText(focusCustomer.getPostalCode());
            for (Division division : DataProvider.getAllDivisions()) {
                if (division.getDivision_ID() == focusCustomer.getDivision_ID()) {
                    upDivisionComboBox.getItems().addAll(division.getDivision());
                    for (Country country : DataProvider.getAllCountries()) {
                        if (division.getCountry_ID() == country.getCountry_ID()) {
                            int focusCountryID = country.getCountry_ID();
                            for (Division d2 : DataProvider.getAllDivisions()) {
                                if (d2.getCountry_ID() == focusCountryID) {
                                    upDivisionComboBox.getItems().addAll(d2.getDivision());
                                }
                            }
                            upCountryComboBox.getItems().addAll(country.getCountry());
                            break;
                        }
                    }
                    upCountryComboBox.getSelectionModel().clearAndSelect(0);
                    upDivisionComboBox.getSelectionModel().select(0);
                    break;
                }
            }
        }
    }

    /**
     * This method takes in an action event for a selected
     * @param actionEvent
     * @throws Exception
     */
    public void clickApptButton(ActionEvent actionEvent) throws Exception {

        try {
            DataProvider.clearCurrentCustomer();
        }
        catch (Exception e) {
            System.out.println("No Customer to clear");
        }
        System.out.println("Clearing Current Customer. Cleared. Current customer: ");
        try {
            Customer focusCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (focusCustomer == null) {
                throw new Exception("focusCustomer is null");
            }
            DataProvider.setCurrentCustomer(focusCustomer);
        }
        catch (Exception e){
            System.out.println(e);
        }
        if (!DataProvider.getCurrentCustomer().isEmpty()) {
            Stage stage = null;
            start(stage, "/view/AppointmentScreen.fxml");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Customer Selected.");
            alert.setContentText("Please select a customer from the table before clicking Make Appointment");
            alert.show();
        }
    }

    public void start(Stage stage, String fxmlTarget) throws Exception {

        //calls jumpscreen scene after successful login
        Parent root = FXMLLoader.load(getClass().getResource(fxmlTarget));
        stage = (Stage) addCustomerButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This method generates the reports
     * @param actionEvent
     * @throws Exception
     */
    public void clickReportsButton(ActionEvent actionEvent) throws Exception {
        Stage stage = null;
        start(stage, "/view/ReportsScreen.fxml");
    }
}
