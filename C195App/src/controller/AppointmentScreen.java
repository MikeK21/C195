package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Appointment Screen Controller handles the landing page after logging in and electing a customer to "Make Appointment"
 * This controller carries over the current user logged in as well as the current customer from the DataProvider class
 * This class initializes the screen and the javafx resources, as well as handling updates
 */
public class AppointmentScreen implements Initializable {


    public TextField apptTitleField;
    public TextField apptDescField;
    public TextField apptLocField;
    public TextField apptStartField;
    public TextField appEndField;
    public TextField apptCustIDField;
    public TextField apptUserIDField;
    public Button addApptButton;
    public TextField upCustomerID;
    public TextField upApptTitleField;
    public TextField upApptDescrField;
    public TextField upApptLocField;
    public TextField upApptStartTime;
    public TextField upApptEndTime;
    public TextField upApptCustIDField;
    public TextField upApptUserIDField;
    public Button upApptButton;
    public Button delApptButton;
    public Button selectButton;
    public TextField upApptTypeField;
    public TextField apptTypeField;
    public ComboBox contactComboBox;
    public ComboBox upContactComboBox;
    public TextField upApptID;
    public Button backButton;
    public DatePicker addStartDatePicker;
    public DatePicker addEndDatePicker;
    public DatePicker upStartDatePicker;
    public DatePicker upEndDatePicker;
    public ComboBox addStartTimeBox;
    public ComboBox addEndTimeBox;
    public ComboBox upStartTimeBox;
    public ComboBox upEndTimeBox;
    public Button reportsButton;
    public Button weekButton;
    public Button monthButton;
    public ComboBox monthComboBox;
    @FXML
    //private ObservableList<Appointment> apptList = FXCollections.observableArrayList();
    // Use Java Collections to create the List.
    List<Appointment> apptArraylist = new ArrayList<Appointment>();
    private ObservableList<Appointment> apptList = FXCollections.observableList(apptArraylist);
    @FXML
    private TableView<Appointment> apptTable;
    // Initialize the columns
    @FXML
    private TableColumn<?, ?> appointmentID;
    @FXML
    private TableColumn<?, ?> appointmentTitle;
    @FXML
    private TableColumn<?, ?> appointmentDescription;
    @FXML
    private TableColumn<?, ?> appointmentLocation;
    @FXML
    private TableColumn<?, ?> appointmentContactID;
    @FXML
    private TableColumn<?, ?> appointmentType;
    @FXML
    private TableColumn<?, ?> appointmentStart;
    @FXML
    private TableColumn<?, ?> appointmentEnd;
    @FXML
    private TableColumn<?, ?> appointmentCustomerID;
    @FXML
    private TableColumn<?, ?> appointmentUserID;

    /**
     * Method that initializes the interactive JavaFX page within the AppointmentScreen Class
     * This method ties values from the database to the actual table view
     */

    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] monthNames = new DateFormatSymbols().getMonths();
        for (String month : monthNames) {
            if (!month.isEmpty()) {
                monthComboBox.getItems().addAll(
                        month
                );
            }
        }

        appointmentID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("Start"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("End"));
        appointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        appointmentUserID.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
        appointmentContactID.setCellValueFactory(new PropertyValueFactory<>("Contact_ID"));

        // Initialize the contact combo boxes
        for (Contact contact : DataProvider.getAllContacts()) {
            contactComboBox.getItems().addAll(
                    contact.getContact_Name()
            );
            upContactComboBox.getItems().addAll(
                    contact.getContact_Name()
            );
        }

        for (LocalTime time : timeGenerator()) {
            addStartTimeBox.getItems().addAll(
                    time.toString()
            );
            addEndTimeBox.getItems().addAll(
                    time.toString()
            );
            upStartTimeBox.getItems().addAll(
                    time.toString()
            );
            upEndTimeBox.getItems().addAll(
                    time.toString()
            );
        }

        // Initialize carried over values
        Customer currentCustomer = DataProvider.getCurrentCustomer().get(0);
        User currentUser = DataProvider.getCurrentUser().get(0);
        // New Appointment Fields
        apptCustIDField.setText(String.valueOf(currentCustomer.getCustomer_ID()));
        apptUserIDField.setText(String.valueOf(currentUser.getUserID()));
        // Update Appointment Fields
        upApptCustIDField.setText(String.valueOf(currentCustomer.getCustomer_ID()));
        upApptUserIDField.setText(String.valueOf(currentUser.getUserID()));


    }

    /**
     *  This returns a contactID. It is known that a contact ID of 0 returned will trigger an alert to select a contact.
     * @param contactBox
     * @return contact.getContact_ID() || 0
     */
    public int getContactID(ComboBox contactBox) {
        if (!contactBox.getSelectionModel().isEmpty()) {
            for (Contact contact : DataProvider.getAllContacts()) {
                if (contactBox.getSelectionModel().getSelectedItem() == contact.getContact_Name()) {
                    return contact.getContact_ID();
                }
            }
        }
        return 0;
    }

    /**
     * Transforms a LocalDate instance and LocalTime instance passed in to a valid LocalDateTime value
     * @param date
     * @param time
     * @return
     */
    public LocalDateTime getSelectedDateTime(LocalDate date, LocalTime time) {

        LocalDateTime startDateTime = null;
        try {
            //startDateTime = LocalDateTime.of(addStartDatePicker.getValue(), (LocalTime) addStartTimeBox.getSelectionModel().getSelectedItem());
            startDateTime = LocalDateTime.of(date,time);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Start Date and Time not selected");
            alert.setContentText("Please select a Date and Time");
            alert.show();
        }
        return startDateTime;
    }

    /**
     * This instantiates the adding of an appointment and does Time Zone conversions if using a none EDT system
     * @param actionEvent
     * @throws Exception
     */
    public void clickAddAppointment(ActionEvent actionEvent) throws Exception {

        ZoneId newYorkZoneId = ZoneId.of("America/New_York");

        int addContactID = getContactID(contactComboBox);
        ArrayList<Contact> contacts = DataProvider.getAllContacts();
        // Debugging Lambda
        //contacts.forEach(contact -> System.out.println(contact) );

        if (addContactID == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Contact Selected");
            alert.setContentText("Please select a Contact from the combo box");
            alert.show();
        }
        try {
            ZonedDateTime startZoned = ZonedDateTime.of(addStartDatePicker.getValue(),
                    LocalTime.parse(addStartTimeBox.getSelectionModel().getSelectedItem().toString()),
                    newYorkZoneId);
            ZonedDateTime endZoned = ZonedDateTime.of(addEndDatePicker.getValue(),
                    LocalTime.parse(addEndTimeBox.getSelectionModel().getSelectedItem().toString()),
                    newYorkZoneId);

            try {
                if (DataProvider.checkApptCollide(startZoned, endZoned, 0)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Appointment Collision");
                    alert.setContentText("Appointment Already scheduled during this time");
                    alert.show();
                } else {
                    Instant startZonedInstant = startZoned.toInstant();
                    Instant endZonedInstant = endZoned.toInstant();
                    LocalDateTime ldtStart = LocalDateTime.ofInstant(startZonedInstant, newYorkZoneId);
                    LocalDateTime ldtEnd = LocalDateTime.ofInstant(endZonedInstant, newYorkZoneId);
                    AppointmentsCRUD.insert(apptTitleField.getText().trim(), apptDescField.getText(), apptLocField.getText(), apptTypeField.getText(), ldtStart, ldtEnd, DataProvider.getCurrentUser().get(0).getUser(), DataProvider.getCurrentUser().get(0).getUser(), Integer.parseInt(apptCustIDField.getText()), DataProvider.getCurrentUser().get(0).getUserID(), addContactID);
                }
            } catch (Exception e) {
                System.out.println("Error Trying to insert into the database");
                System.out.println(e);
            }
        } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Not all fields completed");
                alert.setContentText("Not all fields completed for Appointment Add");
                alert.show();
        }
    }

    /**
     * This table refreshes the active elements on the Appointment Screen such as tableview,etc.
     * @throws Exception
     */
    public void refreshTable() throws Exception {

        apptList.setAll(DataProvider.getFocusAppointments());
        apptTable.setItems(apptList);
    }

    /**
     * This method is for when you select an appointment to be modified either for update or delete.
     * @param actionEvent
     */
    public void clickSelectButton(ActionEvent actionEvent) {
        upContactComboBox.getItems().clear();
        if (apptTable.getSelectionModel().getSelectedItem().getAppointment_ID() > 0) {
            Appointment focusAppointment = apptTable.getSelectionModel().getSelectedItem();
            upApptTitleField.setText(focusAppointment.getTitle());
            upApptDescrField.setText(focusAppointment.getDescription());
            upApptLocField.setText(focusAppointment.getLocation());
            upApptTypeField.setText(focusAppointment.getType());
            upStartDatePicker.setValue(focusAppointment.getStart().toLocalDateTime().toLocalDate());
            upEndDatePicker.setValue(focusAppointment.getEnd().toLocalDateTime().toLocalDate());
            upStartTimeBox.setValue(focusAppointment.getStart().toLocalDateTime().toLocalTime());
            upEndTimeBox.setValue(focusAppointment.getEnd().toLocalDateTime().toLocalTime());
            upApptCustIDField.setText(Integer.toString(focusAppointment.getCustomer_ID()));
            upApptUserIDField.setText(Integer.toString(focusAppointment.getUser_ID()));
            upApptID.setText(String.valueOf(focusAppointment.getAppointment_ID()));
            for (Contact contact : DataProvider.getAllContacts()) {
                if (focusAppointment.getContact_ID() == contact.getContact_ID()) {
                    upContactComboBox.getItems().addAll(
                            contact.getContact_Name()
                    );
                }
            }
        }
    }

    /**
     * This method takes an Action Event and submits an update statement to the database from the selected appt.
     * @param actionEvent
     */
    public void clickUpAppt(ActionEvent actionEvent) {

        ZoneId newYorkZoneId = ZoneId.of("America/New_York");

        int upContactID = getContactID(upContactComboBox);
        if (upContactID == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Contact Selected");
            alert.setContentText("Please select a Contact from the combo box");
            alert.show();
        }


        ZonedDateTime startZoned = ZonedDateTime.of(upStartDatePicker.getValue(),
                LocalTime.parse(upStartTimeBox.getSelectionModel().getSelectedItem().toString()),
                newYorkZoneId);
        ZonedDateTime endZoned = ZonedDateTime.of(upEndDatePicker.getValue(),
                LocalTime.parse(upEndTimeBox.getSelectionModel().getSelectedItem().toString()),
                newYorkZoneId);

        try {
                if (DataProvider.checkApptCollide(startZoned, endZoned,Integer.parseInt(upApptID.getText().trim().toString()))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Appointment Collision");
                    alert.setContentText("Appointment Already scheduled during this time");
                    alert.show();
                }
                else {
                    Instant startZonedInstant = startZoned.toInstant();
                    Instant endZonedInstant = endZoned.toInstant();
                    LocalDateTime ldtStart = LocalDateTime.ofInstant(startZonedInstant, newYorkZoneId);
                    LocalDateTime ldtEnd = LocalDateTime.ofInstant(endZonedInstant, newYorkZoneId);
                    try {
                        AppointmentsCRUD.update(upApptTitleField.getText().trim(), upApptDescrField.getText(), upApptLocField.getText(), upApptTypeField.getText(), ldtStart, ldtEnd, DataProvider.getCurrentUser().get(0).getUser(), Integer.parseInt(apptCustIDField.getText()), DataProvider.getCurrentUser().get(0).getUserID(), upContactID, Integer.parseInt(upApptID.getText()));
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Not all fields completed");
                        alert.setContentText("Not all fields completed for Appointment Update");
                        alert.show();
                    }
                    refreshTable();
                }
        } catch (Exception e) {
            System.out.println("Error Trying to insert into the database");
            System.out.println(e);
        }
    }

    /**
     * This method takes an Action Event and submits a delete statement to the database from the selected appt.
     * @param actionEvent
     * @throws Exception
     */
    public void clickDelAppt(ActionEvent actionEvent) throws Exception {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setContentText("Are you sure you'd like to cancel the selected appointment?");
        Optional<ButtonType> result = alert.showAndWait();
        boolean deleteConfirm = false;
        if(result.isPresent() && result.get() == ButtonType.OK) {
            deleteConfirm = true;
        }
        if (deleteConfirm) {
            AppointmentsCRUD.delete(Integer.parseInt(upApptID.getText()));
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Cancel Successful");
            String infoMessage = "Appointment ID " + upApptID.getText() + " of  type " + upApptTypeField.getText() + " canceled";
            info.setContentText(infoMessage);
            info.show();
            refreshTable();
        }
    }

    /**
     * This method takes a stage and fxml target to pull up a dynamically called scene target.
     * @param stage
     * @param fxmlTarget
     * @throws Exception
     */
    public void start(Stage stage, String fxmlTarget) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlTarget));
        stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * THis method is for the back button to return to the customer screen.
     * @param actionEvent
     * @throws Exception
     */
    public void clickBackButton(ActionEvent actionEvent) throws Exception {
        apptList.clear();
        DataProvider.clearCurrentCustomer();
        apptTable.getItems().clear();
        System.out.println("Back Button Clicked");
        Stage stage = null;
        start(stage, "/view/CustomerScreen.fxml");
    }

    /**
     * Takes in ZonedDateTime parameters, as well as an ApptID int, to determine if theres an appt conflict.
     * If theres a collision, it returns true. If no collision exists, it returns false.
     * @param startTime
     * @param endTime
     * @param ApptId
     * @return
     */
    public static boolean checkApptCollide(ZonedDateTime startTime, ZonedDateTime endTime, Integer ApptId) throws Exception {

        Instant passedStartInstant = startTime.toInstant();
        Instant passedEndInstant = endTime.toInstant();

        for (Appointment appointment : DataProvider.getAllAppointments()) {
            Instant apptStartInstant = appointment.getStart().toInstant();
            Instant apptEndInstant = appointment.getEnd().toInstant();
            if (appointment.getAppointment_ID() == ApptId) {
                continue;
            }

            if ( passedStartInstant.compareTo(apptStartInstant) >= 0 && passedStartInstant.compareTo(apptEndInstant) <= 0) {
                return true;
            }
            else if (passedEndInstant.compareTo(apptStartInstant) >= 0 && passedEndInstant.compareTo(apptEndInstant) <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is called without parameters and returns a list of times according to system Zoned Data Time
     * Before submitting appointment for addition or an update, the time is converted to the backend systems time zone
     * @return
     */
    public static List<LocalTime> timeGenerator() {

        List<LocalTime> timeList = new ArrayList<LocalTime>();

        LocalDate todayDate = LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();



        for (int i = 8; i <= 22; i++) {
            int hourHolder = i;
            int minuteHolder = 0;
            int k = 2;
            while (k < 4) {
                int minuteFinder = k % 2;
                if (minuteFinder != 0) {
                    minuteHolder = 30;
                }
                LocalTime midnight = LocalTime.of(hourHolder, minuteHolder);
                LocalDateTime dateTime = LocalDateTime.of(todayDate, midnight);
                ZonedDateTime localDateTime = ZonedDateTime.of(dateTime,zone);
                ZonedDateTime estDateTime = localDateTime.withZoneSameInstant(zone);
                LocalTime convertedTime = estDateTime.toLocalTime();

                timeList.add(convertedTime);
                k++;
            }
        }
        return timeList;
    }

    public void clickWeekButton(ActionEvent actionEvent) throws Exception {

        //Clear out any old filtered list
        refreshTable();
        DataProvider.clearWeeksAppointments();
        try {
            WeekMonthAppointmentCRUD.selectWeek(DataProvider.getCurrentCustomer().get(0).getCustomer_ID());
        }
        catch (Exception e) {
            System.out.println(e);
        }
        apptList.clear();
        apptList.setAll(DataProvider.getWeeksAppointments());
        apptTable.setItems(apptList);
    }

    public void selectMonthComboBox(ActionEvent actionEvent) throws Exception {
        //Clear out any old filtered list
        refreshTable();
        DataProvider.clearMonthsAppointments();
        try {
            WeekMonthAppointmentCRUD.selectMonth(DataProvider.getCurrentCustomer().get(0).getCustomer_ID(), monthComboBox.getSelectionModel().getSelectedItem().toString());
        }
        catch (Exception e) {
            if (monthComboBox.getSelectionModel().getSelectedItem().toString().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Month Selected");
                alert.setContentText("Please select a month to filter by month");
                alert.showAndWait();
            }
            System.out.println(e);
        }
        apptList.clear();
        apptList.setAll(DataProvider.getMonthsAppointments());
        apptTable.setItems(apptList);
    }
}
