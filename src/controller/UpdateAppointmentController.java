package controller;

import DAO.AppointmentsQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class takes in entered data and updates an existing appointment.
 */
public class UpdateAppointmentController implements Initializable {
    @FXML
    private RadioButton remoteRBtn;
    @FXML
    private RadioButton inOfficeRBtn;
    @FXML
    private TextField apptmtIdTxt;
    @FXML
    private ComboBox<Contacts> contactsCombo;
    @FXML
    private ComboBox<Customers> customersCombo;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField descTxt;
    @FXML
    private ComboBox<LocalTime> endCombo;
    @FXML
    private TextField locTxt;
    @FXML
    private ComboBox<LocalTime> startCombo;
    @FXML
    private TextField titleTxt;
    @FXML
    private TextField typeTxt;
    @FXML
    private ComboBox<Users> usersCombo;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * This method receives the appointment selected in the AppointmentsInfo scene and populates the UpdateAppointment
     * fields with the appointment's current values.
     *
     * @param appointment The appointment to be updated.
     */
    public void sendAppointment(Appointments appointment) {
        apptmtIdTxt.setText(String.valueOf(appointment.getApptmtId()));
        titleTxt.setText(appointment.getTitle());
        descTxt.setText(appointment.getDescription());
        locTxt.setText(appointment.getLocation());
        typeTxt.setText(appointment.getType());
        datePicker.setValue(appointment.getStart().toLocalDate());

        for (Contacts contact : Contacts.getAllContacts()) {
            if (contact.getContactId() == appointment.getContactId())
                contactsCombo.setValue(contact);
        }

        for (Customers customer : Customers.getAllCustomers()) {
            if (customer.getCustId() == appointment.getCustId())
                customersCombo.setValue(customer);
        }

        for (Users user : Users.getAllUsers()) {
            if (user.getUserId() == appointment.getUserId())
                usersCombo.setValue(user);
        }

        for (LocalTime startTime : Times.getBusinessHours()) {
            if (startTime.equals(appointment.getStart().toLocalTime())) {
                startCombo.setValue(startTime);
            }
        }

        for (LocalTime endTime : Times.getBusinessHoursEnd()) {
            if (endTime.equals(appointment.getEnd().toLocalTime())) {
                endCombo.setValue(endTime);
            }
        }

        if (appointment instanceof Remote) {
            remoteRBtn.setSelected(true);

        }
        if (appointment instanceof  InOffice) {
            inOfficeRBtn.setSelected(true);
        }
    }


    /**
     * This method returns the application to the AppointmentsInfo scene.
     *
     * @param event The back button is clicked.
     */
    @FXML
    public void onActionBack(ActionEvent event) throws IOException {
        Alert backConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        backConfirmation.setHeight(400);
        backConfirmation.setContentText("If you would like to go back the data for this appointment will not be saved. Please confirm you would like to proceed.");

        Optional<ButtonType> result = backConfirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            SceneSwitcher AppointmentScene = new SceneSwitcher();
            AppointmentScene.buttonSwitchScene("/view/AppointmentsInfo.fxml", event);
        }


    }


    /**
     * This method updates the appointment with the entered data.
     * After update is executed, an update successful or update failed alert will be displayed.
     * The application then returns to the AppointmentsInfo scene.
     *
     * @param event The save button is clicked.
     */
    @FXML
    public void onActionSaveApptmt(ActionEvent event) throws IOException, SQLException {
        try {
            int apptmtId = Integer.parseInt(apptmtIdTxt.getText());
            String title = titleTxt.getText();
            String desc = descTxt.getText();
            String loc = locTxt.getText();
            String contactName = contactsCombo.getValue().getContactName();
            int contactId = contactsCombo.getValue().getContactId();
            String type = typeTxt.getText();
            String user = usersCombo.getValue().getUserName();
            int userId = usersCombo.getValue().getUserId();
            int custId = customersCombo.getValue().getCustId();


            String venue = null;

            if (inOfficeRBtn.isSelected()) {
                venue = "In-Office";
            }

            if (remoteRBtn.isSelected()) {
                venue = "In-Office";
            }

            LocalDate apptmtDate = datePicker.getValue();
            LocalTime startTime = startCombo.getValue();
            LocalTime endTime = endCombo.getValue();

            LocalDateTime start = LocalDateTime.of(apptmtDate, startTime);
            LocalDateTime end = LocalDateTime.of(apptmtDate, endTime);

            LocalDateTime overlapApptmt = Appointments.overlap(start, end, apptmtId);

            if (overlapApptmt != null) {

                Alert apptmtOverlap = new Alert(Alert.AlertType.ERROR);
                apptmtOverlap.setTitle("Appointment Overlap");
                apptmtOverlap.setContentText("This appointment overlaps with an appointment beginning at " + dateFormat.format(overlapApptmt) + ". Please select another appointment time and try again.");
                apptmtOverlap.showAndWait();
            } else {
                int rowsAffected = AppointmentsQuery.update(apptmtId, title, loc, desc, contactId, contactName, type, start, end, user, custId, userId, venue);

                if (rowsAffected > 0) {
                    Alert updateSuccess = new Alert(Alert.AlertType.INFORMATION);
                    updateSuccess.setTitle("Update Success");
                    updateSuccess.setContentText("Appointment update successful.");
                    updateSuccess.showAndWait();
                } else {
                    Alert updateFailed = new Alert(Alert.AlertType.INFORMATION);
                    updateFailed.setTitle("Update Failed");
                    updateFailed.setContentText("Appointment update has failed.");
                    updateFailed.showAndWait();
                }

                SceneSwitcher AppointmentsScene = new SceneSwitcher();
                AppointmentsScene.buttonSwitchScene("/view/AppointmentsInfo.fxml", event);
            }
        }
        catch (Exception e) {
            Alert invalidInputError = new Alert(Alert.AlertType.ERROR);
            invalidInputError.setTitle("Invalid Input Error");
            invalidInputError.setContentText("Please enter a valid value for each field.");
            invalidInputError.showAndWait();

            e.printStackTrace();
        }
    }


    /**
     * This is the initialize method.
     * This method will populate the contacts ComboBox with all contacts, the customers ComboBox
     * with all customers, and the users ComboBox with all users. The start time and end time
     * ComboBoxes will be populated with hours matching the company's business hours
     * translated to the user's local time.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactsCombo.setItems(Contacts.getAllContacts());
        usersCombo.setItems(Users.getAllUsers());
        customersCombo.setItems(Customers.getAllCustomers());
        Times.prepareBusinessHours();
        startCombo.setItems(Times.getBusinessHours());
        endCombo.setItems(Times.getBusinessHoursEnd());
    }


}
