package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class DataProvider {

    // Users DAO
    public static ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    private static ArrayList<User> allUsers = new ArrayList<User>();
    public static void addUser(User user) {
        allUsers.add(user);
    }
    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    // Country DAO
    public static ArrayList<Country> currentCountry = new ArrayList<>();
    public static void setCurrentCountry(Country country) { currentCountry.add(country); }
    public static ArrayList<Country> getCurrentCountry() {return currentCountry; }

    // Current User DAO
    private static ArrayList<User> currentUser = new ArrayList<User>();
    public static void setCurrentUser(User user) { currentUser.add(user); }
    public static ArrayList<User> getCurrentUser() {
        return currentUser;
    }

    // Customer DAO
    private static ArrayList<Customer> allCustomers = new ArrayList<Customer>();
    public static void clearCustomers() {
        allCustomers.clear();
    }
    public static void addCustomer(Customer customer) {
        allCustomers.add(customer);
        customerObservableList.add(customer);
    }
    public static ArrayList<Customer> getAllCustomers() {
        return allCustomers;
    }
    public static ObservableList<Customer> getCustomerObservableList() { return customerObservableList;}

    // Current Customer DAO
    private static ArrayList<Customer> currentCustomer = new ArrayList<>();
    public static void setCurrentCustomer(Customer customer) {
        currentCustomer.clear();
        currentCustomer.add(customer);
    }
    public static ArrayList<Customer> getCurrentCustomer() {
        return currentCustomer;
    }
    public static void clearCurrentCustomer() {
        currentCustomer.clear();
    }

    // Division DAO
    private static ArrayList<Division> allDivisions = new ArrayList<Division>();
    public static void addDivision(Division division) {allDivisions.add(division);}
    public static ArrayList<Division> getAllDivisions() {return allDivisions;}

    // Focus Division DAO
    private static ArrayList<Division> focusDivision = new ArrayList<Division>();
    public static void setFocusDivision(String countryName) {
        focusDivision.clear();
        for (Country c : allCountries) {
            if (c.getCountry() == countryName) {
                for (Division d : allDivisions) {
                    if (d.getCountry_ID() == c.getCountry_ID()) {
                        focusDivision.add(d);
                    }
                }
            }
        }
    }
    public static ArrayList<Division> getFocusDivision() {return focusDivision;}

    // Countries DAO
    private static ArrayList<Country> allCountries = new ArrayList<Country>();
    public static void addCountry(Country country) {allCountries.add(country);}
    public static ArrayList<Country> getAllCountries() {return allCountries;}

    // Contact DAO
    private static ArrayList<Contact> allContacts = new ArrayList<>();
    public static void addContact(Contact contact) {allContacts.add(contact);}
    public static ArrayList<Contact> getAllContacts() {return allContacts;}

    // Appointment DAO
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    public static void clearAppointments() {
        allAppointments.clear();
    }
    public static void addAppointment(Appointment appointment) {allAppointments.add(appointment); };
    public static ObservableList<Appointment> getAllAppointments() throws Exception {
        clearAppointments();
        AppointmentsCRUD.select();
        return allAppointments; };

    // Todays Appointment (Report) DAO
    private static ObservableList<Appointment> todaysAppointments = FXCollections.observableArrayList();
    public static void clearTodaysAppointments() {
        todaysAppointments.clear();
    }
    public static void addTodaysAppointment(Appointment appointment) {todaysAppointments.add(appointment); };
    public static ObservableList<Appointment> getTodaysAppointments() {return todaysAppointments; };


    // Months Appointment DAO
    private static ObservableList<Appointment> MonthsAppointments = FXCollections.observableArrayList();
    public static void clearMonthsAppointments() {
        MonthsAppointments.clear();
    }
    public static void addMonthsAppointment(Appointment appointment) {MonthsAppointments.add(appointment); };
    public static ObservableList<Appointment> getMonthsAppointments() {return MonthsAppointments; };

    // Weeks Appointment DAO
    private static ObservableList<Appointment> WeeksAppointments = FXCollections.observableArrayList();
    public static void clearWeeksAppointments() {
        WeeksAppointments.clear();
    }
    public static void addWeeksAppointment(Appointment appointment) {WeeksAppointments.add(appointment); };
    public static ObservableList<Appointment> getWeeksAppointments() {return WeeksAppointments; };


    // Appointments By Type and Month (Report) DAO
    private static ObservableList<Appointment> typeMonthAppointments = FXCollections.observableArrayList();
    public static void clearTypeMonthAppointments() {
        typeMonthAppointments.clear();
    }
    public static void addTypeMonthAppointment(Appointment appointment) {typeMonthAppointments.add(appointment); };
    public static ObservableList<Appointment> getTypeMonthAppointments() {return typeMonthAppointments; };

    // Contact Map
    public static HashMap<String, Integer> getContactMap() {
        HashMap<String, Integer> contactMap = new HashMap<String, Integer>();
        for (Contact contact : allContacts) {
            contactMap.put(contact.getContact_Name(), contact.getContact_ID());
        }
        return contactMap;
    };

    // Appointments by Contact (Report) DAO
    private static ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
    public static void clearContactAppointments() {
        contactAppointments.clear();
    }
    public static void addContactAppointment(Appointment appointment) { contactAppointments.add(appointment); };
    public static ObservableList<Appointment> getContactAppointments() {return contactAppointments; };

    // Focus Appointments DAO
    private static ObservableList<Appointment> focusAppointments = FXCollections.observableArrayList();
    public static ObservableList<Appointment> getFocusAppointments() throws Exception {
        focusAppointments.clear();
        AppointmentsCRUD.select();
        for (Appointment appointment : DataProvider.getAllAppointments()) {
            System.out.println(appointment.getDescription());
            if (appointment.getCustomer_ID() == DataProvider.getCurrentCustomer().get(0).getCustomer_ID()) {
                focusAppointments.add(appointment);
            }
        }
            return focusAppointments;
    }

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
     * Checks for Upcoming appointments based on Customer/Customer ID.
     */
    public static void checkForAppt() throws Exception {
        User currentUser = DataProvider.getCurrentUser().get(0);
        boolean apptCollide = false;
        Instant instantStartTime = Instant.now();
        Integer targetID = 0;
        Timestamp targetTime = Timestamp.from(Instant.now());
        for (Appointment appointment : DataProvider.getAllAppointments()) {
            if (appointment.getUser_ID() == currentUser.getUserID()) {
                instantStartTime = appointment.getStart().toInstant();
                Instant instantTimeNow = Instant.now();
                if (instantStartTime.getEpochSecond() > instantTimeNow.getEpochSecond() && instantStartTime.getEpochSecond() < instantTimeNow.plus(15, ChronoUnit.MINUTES).getEpochSecond()) {
                    apptCollide = true;
                    targetID = appointment.getAppointment_ID();
                    targetTime = appointment.getStart();
                }
            }
        }
        if (apptCollide) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Soon!");
            alert.setContentText("You have an appointment for Appointment ID:  " + targetID.toString() +
                    " starting at " + targetTime.toString());
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Upcoming Appointments");
            alert.setContentText("There are no upcoming appointments on record");
            alert.show();
        }
    }


}
