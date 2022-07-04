package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import utils.DataProvider;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This class implements the month type report screen after clicking "generate" in the ReportsScreen.s
 */
public class MonthAppointmentScreen implements Initializable {

    public TableView MonthTypeApptTable;
    List<Appointment> apptArraylist = new ArrayList<Appointment>();
    private ObservableList<Appointment> apptList = FXCollections.observableList(apptArraylist);
    public Button backButton;
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




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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


        try {
            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This allows the user to go back to the reports screen.
     * @param actionEvent
     * @throws Exception
     */
    public void clickBackButton(ActionEvent actionEvent) throws Exception {
        Stage stage = null;
        start(stage, "/view/ReportsScreen.fxml");
    }

    public void start(Stage stage, String fxmlTarget) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlTarget));
        stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This refreshes the active table
     * @throws Exception
     */
    public void refreshTable() throws Exception {

        apptList.setAll(DataProvider.getTypeMonthAppointments());
        MonthTypeApptTable.setItems(apptList);
    }
}
