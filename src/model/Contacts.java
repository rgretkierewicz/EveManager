package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class is used for creating, modifying, and accessing data for contacts. */
public class Contacts  {
    private int contactId;
    private String contactName;
    private String email;
    private static ObservableList<Contacts> allContacts = FXCollections.observableArrayList();

    public Contacts(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * @return the contact id
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName the contact name to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * @return all contacts
     */
    public static ObservableList<Contacts> getAllContacts() {
        return allContacts;
    }

    /**
     * @param newContact the contaact to add
     */
    public static void addContact(Contacts newContact) {
        allContacts.add(newContact);
    }

    /**
     * @return contact id and contact name
     */
    @Override
    public String toString() {
        return "[" + contactId + "] " + contactName;
    }





}
