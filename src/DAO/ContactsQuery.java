package DAO;

import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class executes a query to the 'contacts' table of the database.*/
public class ContactsQuery {

    /**
     * This method queries the contacts table to return all contacts. The contact data
     * returned is used to create matching Contacts objects within the application.
     * @throws SQLException
     */
    public static void contactsCreation() throws SQLException {
        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int contactId = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String email =  rs.getString("Email");

            Contacts newContact = new Contacts(contactId, contactName, email);
            Contacts.addContact(newContact);
        }
    }

}
