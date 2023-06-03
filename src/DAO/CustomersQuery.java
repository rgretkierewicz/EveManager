package DAO;

import model.Customers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class executes queries, updates, inserts, and deletes to the 'customers' table of the database.
 */
public class CustomersQuery {

    /**
     * This method queries the customers table to return all customers. The customer data
     * returned is used to create matching Customers objects within the application.
     *
     * @throws SQLException
     */
    public static void customersCreation() throws SQLException {
        String sql = "SELECT * FROM customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int custId = rs.getInt("Customer_ID");
            String custName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            int divId = rs.getInt("Division_ID");
            String user = null;


            ps = JDBC.connection.prepareStatement("SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?");
            ps.setInt(1, divId);
            ResultSet rsC = ps.executeQuery();
            rsC.next();
            int countryId = rsC.getInt(1);

            ps = JDBC.connection.prepareStatement("SELECT Country FROM countries WHERE Country_ID = ?");
            ps.setInt(1, countryId);
            rsC = ps.executeQuery();
            rsC.next();
            String country = rsC.getString(1);

            Customers newCustomer = new Customers(custId, custName, address, postalCode, phone, divId, country, user);
            Customers.addCustomer(newCustomer);
        }
    }

    /**
     * This method takes in all customer values and inserts that customer into the database.
     * The application's customer object is created.
     *
     * @param custName   the customer's name
     * @param address    the customer's address
     * @param postalCode the customer's postal code
     * @param phone      the customer's phone number
     * @param divId      the customer's division id
     * @param user       the user account that the customer belongs to
     * @throws SQLException
     */
    public static void insert(String custName, String address, String postalCode, String phone, int divId, String user) throws SQLException {
        String sql = "INSERT INTO customers VALUES(NULL, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, custName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, user);
        ps.setString(6, user);
        ps.setInt(7, divId);
        ps.execute();

        ps = JDBC.connection.prepareStatement("SELECT LAST_INSERT_ID()"); // FROM customers
        ResultSet rs = ps.executeQuery();
        rs.next();
        int custId = rs.getInt(1);

        String country = CustomersQuery.getCountry(divId);

        Customers newCustomer = new Customers(custId, custName, address, postalCode, phone, divId, country, user);
        newCustomer.setUser(user);
        Customers.addCustomer(newCustomer);
    }

    /**
     * This method takes in all customer values and updates that customer in the database.
     * The application's customer object is updated.
     *
     * @param custName   the customer's name
     * @param address    the customer's address
     * @param postalCode the customer's postal code
     * @param phone      the customer's phone number
     * @param divId      the customer's division id
     * @param user       the user account that the customer belongs to
     * @return 1 if update is successful, 0 if update fails
     * @throws SQLException
     */
    public static int update(int custId, String custName, String address, String postalCode, String phone, int divId, String user) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, custName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, user);
        ps.setInt(6, divId);
        ps.setInt(7, custId);

        int rowsAffected = ps.executeUpdate();

        String country = CustomersQuery.getCountry(divId);

        Customers updatedCust = new Customers(custId, custName, address, postalCode, phone, divId, country, user);

        int index = -1;
        for (Customers cust : Customers.getAllCustomers()) {
            index++;

            if (cust.getCustId() == custId) {
                Customers.updateCustomer(index, updatedCust);
            }
        }


        return rowsAffected;
    }

    /**
     * This method removes a customer from the database.
     *
     * @param custId the id of the customer to be deleted
     * @return 1 if delete is successful, 0 if delete fails
     * @throws SQLException
     */
    public static int delete(int custId) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, custId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * @param divId the division id to be searched
     * @return the country associated with the division id
     * @throws SQLException
     */
    private static String getCountry(int divId) throws SQLException {
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?");
        ps.setInt(1, divId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int countryId = rs.getInt(1);

        ps = JDBC.connection.prepareStatement("SELECT Country FROM countries WHERE Country_ID = ?");
        ps.setInt(1, countryId);
        rs = ps.executeQuery();
        rs.next();
        String country = rs.getString(1);

        return country;

    }

}
