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
 * This class creates a new appointment.
 */
public class AddAppointmentController implements Initializable {
    @FXML
    private RadioButton remoteRBtn;
    @FXML
    private RadioButton inOfficeRBtn;
    @FXML
    private TextField apptmtIdTxt;
    @FXML
    private TextField typeTxt;
    @FXML
    private ComboBox<Contacts> contactsCombo;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<LocalTime> endCombo;
    @FXML
    private ComboBox<LocalTime> startCombo;
    @FXML
    private TextField descTxt;
    @FXML
    private TextField locTxt;
    @FXML
    private TextField titleTxt;
    @FXML
    private ComboBox<Customers> customersCombo;
    @FXML
    private ComboBox<Users> usersCombo;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * This method will trigger an alert that requires the user to confirm that they would like to go back without saving.
     * Upon confirmation, the user is returned to the application's AppointmentsInfo scene.
     * If the user hits "Cancel", they will remain on the current scene.
     *
     * @param event The Back button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionBack(ActionEvent event) throws IOException {
        Alert backConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        backConfirmation.setTitle("Appointment Not Saved");
        backConfirmation.setHeight(400);
        backConfirmation.setContentText("If you would like to go back the data for this appointment will not be saved. Please confirm you would like to proceed.");

        Optional<ButtonType> result = backConfirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            SceneSwitcher AppointmentScene = new SceneSwitcher();
            AppointmentScene.buttonSwitchScene("/view/AppointmentsInfo.fxml", event);
        }

    }

    /**
     * This method creates a new appointment from the entered data.
     * The application then returns to the AppointmentsInfo scene.
     *
     * @param event The Save button is clicked.
     */
    @FXML
    public void onActionSaveApptmt(ActionEvent event) throws SQLException, IOException {
        try {
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

            LocalDateTime overlapApptmt = Appointments.overlap(start, end);

            if (overlapApptmt != null) {
                Alert apptmtOverlap = new Alert(Alert.AlertType.ERROR);
                apptmtOverlap.setTitle("Appointment Overlap");
                apptmtOverlap.setContentText("This appointment overlaps with an appointment beginning at " + dateFormat.format(overlapApptmt) + ". Please select another appointment time and try again.");
                apptmtOverlap.showAndWait();
            } else {
                AppointmentsQuery.insert(title, desc, loc, contactId, contactName, type, start, end, user, custId, userId, venue);

                SceneSwitcher AppointmentsScene = new SceneSwitcher();
                AppointmentsScene.buttonSwitchScene("/view/AppointmentsInfo.fxml", event);
            }
        } catch (Exception e) {
            Alert invalidInputError = new Alert(Alert.AlertType.ERROR);
            invalidInputError.setTitle("Invalid Input Error");
            invalidInputError.setContentText("Please enter a valid value for each field.");
            invalidInputError.showAndWait();
        }

    }

    /**
     * This is the initialize method.
     * This method will populate the contacts, customers, and users ComboBoxes.
     * The start time and end time ComboBoxes will be populated with hours matching
     * the company's business hours translated to the user's local time.
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
