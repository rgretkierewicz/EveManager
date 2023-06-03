package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointments;
import model.Contacts;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This class allows a user to select a contact, then displays all appointments scheduled
 * for that contact.
 */
public class ScheduleReportsController implements Initializable {
    @FXML
    private TableColumn<Appointments, Integer> appIdCol;
    @FXML
    private TableView<Appointments> contactApptmtsTable;
    @FXML
    private TableColumn<Appointments, String> contactCol;
    @FXML
    private ComboBox<Contacts> contactsCombo;
    @FXML
    private TableColumn<Appointments, Integer> custIdCol;
    @FXML
    private TableColumn<Appointments, String> descCol;
    @FXML
    private TableColumn<Appointments, LocalDateTime> endCol;
    @FXML
    private TableColumn<Appointments, String> locCol;
    @FXML
    private TableColumn<Appointments, LocalDateTime> startCol;
    @FXML
    private TableColumn<Appointments, String> titleCol;
    @FXML
    private TableColumn<Appointments, String> typeCol;
    @FXML
    private TableColumn<Appointments, Integer> userIdCol;


    /**
     * This method changes the scene to the AppointmentInfo scene.
     *
     * @param event The Appointments Info button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionAppointmentsInfo(ActionEvent event) throws IOException {
        SceneSwitcher AppointmentsInfoScene = new SceneSwitcher();
        AppointmentsInfoScene.buttonSwitchScene("/view/AppointmentsInfo.fxml", event);
    }

    /**
     * This method changes the scene to the CustomersInfo scene.
     *
     * @param event The Customer Info button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionCustomerInfo(ActionEvent event) throws IOException {
        SceneSwitcher CustomersInfoScene = new SceneSwitcher();
        CustomersInfoScene.buttonSwitchScene("/view/CustomersInfo.fxml", event);
    }

    /**
     * This method changes the scene to the Reports scene.
     *
     * @param event The Reports button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionReports(ActionEvent event) throws IOException {
        SceneSwitcher ReportsScene = new SceneSwitcher();
        ReportsScene.buttonSwitchScene("/view/Reports.fxml", event);
    }

    /**
     * This method changes the scene to the AppointmentInfo scene.
     *
     * @param event The Appointments Info button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionBack(ActionEvent event) throws IOException {
        SceneSwitcher ReportsScene = new SceneSwitcher();
        ReportsScene.buttonSwitchScene("/view/Reports.fxml", event);

    }

    /**
     * This method populates the divisions ComboBox based off of selected country.
     *
     * @param event A country is selected from the country ComboBox.
     */
    @FXML
    public void onActionContactSelect(ActionEvent event) {
        Contacts thisContact = contactsCombo.getSelectionModel().getSelectedItem();

        ObservableList<Appointments> contactAppointments = FXCollections.observableArrayList();

        for (Appointments apptmt : Appointments.getAllAppointments()) {
            if (apptmt.getContactId() == thisContact.getContactId()) {
                contactAppointments.add(apptmt);
            }
        }
        contactApptmtsTable.setItems(contactAppointments);
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("apptmtId"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    /**
     * This is the initialize method. This method will populate the contacts ComboBox.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactsCombo.setItems(Contacts.getAllContacts());
    }
}


