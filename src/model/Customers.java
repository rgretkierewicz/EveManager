package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class is used for creating, modifying, and accessing data for customers. */
public class Customers  {
    private static ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Customers> filteredCustomers = FXCollections.observableArrayList();
    private int custId;
    private String custName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;
    private String country;
    private String user;
    static boolean resultsFoundC = true;

    public Customers(int custId, String custName, String address, String postalCode, String phone, int divisionId, String country, String user) {
        this.custId = custId;
        this.custName = custName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.country = country;
        this.user = user;
    }

    /**
     * @return the division id
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId the division id to set
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @param newCustomer the customer to add
     */
    public static void addCustomer(Customers newCustomer) {
        allCustomers.add(newCustomer);
    }

    /**
     * @return all customers
     */
    public static ObservableList<Customers> getAllCustomers() {
        return allCustomers;
    }

    /**
     * @return the customer's id
     */
    public int getCustId() {
        return custId;
    }

    /**
     * @return the customer's name
     */
    public String getCustName() {
        return custName;
    }

    /**
     * @param custName the customer name to set
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the division id
     */
    public int getDivsionId() {
        return divisionId;
    }

    /**
     * @param divsionId the division id to set
     */
    public void setDivsionId(int divsionId) {
        this.divisionId = divsionId;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    public static Customers lookupCustomer(int custId) {
        for (Customers cust : allCustomers) {
            if (cust.getCustId() == custId) {
                return cust;
            }
        }
        return null;
    }

    public static ObservableList<Customers> lookupCustomer(String custName) {
        if(!(filteredCustomers.isEmpty())) {
            filteredCustomers.clear();
        }

        for (Customers cust : allCustomers) {
            if (cust.getCustName().toLowerCase().contains(custName.toLowerCase())) {
                filteredCustomers.add(cust);
            }
        }
        if (filteredCustomers.isEmpty()) {
            resultsFoundC = false;
            return allCustomers;
        }
        else {
            resultsFoundC = true;
            return filteredCustomers;
        }
    }

    public static boolean resultsFound() {
        return resultsFoundC;
    }

    /**
     * @param customerDelete the customer to delete
     */
    public static void deleteCustomer(Customers customerDelete) {
        allCustomers.remove(customerDelete);
    }

    /**
     * @param index the index of the customer to update
     * @param updatedCust the updated customer
     */
    public static void updateCustomer(int index, Customers updatedCust) {
        allCustomers.set(index, updatedCust);
    }

    /**
     * @return customer id and customer name
     */
    @Override
    public String toString() {
        return "[" + custId + "] " + custName;
    }


}
