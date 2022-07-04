package model;

public class Customer {

    private String postalCode;
    private String Address;
    private String phoneNumber;
    private String Customer_Name;
    private int Customer_ID;
    private int Division_ID;

    public int getDivision_ID() {
        return Division_ID;
    }

    public void setDivision_ID(int division_ID) {
        Division_ID = division_ID;
    }

    public Customer (int Customer_ID, String postalCode, String Address, String phoneNumber, String Customer_Name, int Division_ID) {
        this.Customer_ID = Customer_ID;
        this.postalCode = postalCode;
        this.Address = Address;
        this.phoneNumber = phoneNumber;
        this.Customer_Name = Customer_Name;
        this.Division_ID = Division_ID;
    }


    //public static ObservableList<Customer> allUsers = FXCollections.observableArrayList();


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public int getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

}