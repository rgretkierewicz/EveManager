package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import model.Appointments;
import model.InOffice;
import model.Remote;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.ResourceBundle;

public class AppointmentReportsController implements Initializable {
    @FXML
    private ComboBox<Month> monthCombo;
    @FXML
    private ComboBox<String> typeCombo;
    @FXML
    private ComboBox<String> venueCombo;
    @FXML
    private Label numberLabel;
    String venueSelection;

    Appointments appointments = new Appointments();

    InOffice inOffice = new InOffice();
    Remote remote = new Remote();
    private static ObservableList<String> types = FXCollections.observableArrayList(null,"Planning Session", "De-Briefing", "Intake", "Maintenance");

    private static ObservableList<String> venueList = FXCollections.observableArrayList("All", "Remote", "In-Office");

    private static ObservableList<Month> allMonths = FXCollections.observableArrayList(null ,Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST,
    Month.SEPTEMBER, Month.OCTOBER, Month.DECEMBER);;

    /**
     * This method changes the scene to the AppointmentsInfo scene.
     * @param event The Appointments Info button is clicked
     * @throws IOException
     */
    @FXML
    public void onActionAppointmentsInfo(ActionEvent event) throws IOException {
        SceneSwitcher AppointmentsInfoScene = new SceneSwitcher();
        AppointmentsInfoScene.buttonSwitchScene("/view/AppointmentsInfo.fxml", event);
    }

    /**
     * This method returns the application to the Reports scene.
     * @param event The back button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionBack(ActionEvent event) throws IOException {
        SceneSwitcher ReportsScene = new SceneSwitcher();
        ReportsScene.buttonSwitchScene("/view/Reports.fxml", event);
    }

    /**
     * This method changes the scene to the CustomersInfo scene.
     * @param event The Customers Info button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionCustomerInfo(ActionEvent event) throws IOException {
        SceneSwitcher CustomersInfoScene = new SceneSwitcher();
        CustomersInfoScene.buttonSwitchScene("/view/CustomersInfo.fxml", event);
    }

    public void onActionReports(ActionEvent event) throws IOException {
        SceneSwitcher ReportsScene = new SceneSwitcher();
        ReportsScene.buttonSwitchScene("/view/Reports.fxml", event);
    }

    /**
     * This method will return the number of appointments matching the specified criteria of
     * appointment month, appointment type, or both.
     * @param event The Generate Report button is clicked.
     */
    @FXML
    public void onActionGenerateReport(ActionEvent event){
        String apptmtType = null;
        int apptmtMonth = 0;

        //Getting ComboBox selection where selection has been made
        if (venueCombo.getSelectionModel().getSelectedItem() != null) {
            venueSelection = venueCombo.getSelectionModel().getSelectedItem();
        }
        if (venueCombo.getSelectionModel().getSelectedItem() == null) {
            venueSelection = "All";
        }

        if (typeCombo.getSelectionModel().getSelectedItem() != null) {
            apptmtType = typeCombo.getSelectionModel().getSelectedItem();
        }
        if (monthCombo.getSelectionModel().getSelectedItem() != null) {
            apptmtMonth = monthCombo.getSelectionModel().getSelectedItem().getValue();
        }

        switch (venueSelection) {
            case "All":
                //All selected
                if (apptmtMonth > 0 && apptmtType != null) {
                    numberLabel.setText(String.valueOf(appointments.returnApptmts(apptmtType, apptmtMonth)));
                }
                //Month selected, type is not
                else if (apptmtMonth > 0 && apptmtType == null) {
                    numberLabel.setText(String.valueOf(appointments.returnApptmts(apptmtMonth)));
                }
                //Type selected, month is not
                else if (apptmtMonth == 0 && apptmtType != null) {
                    numberLabel.setText(String.valueOf(appointments.returnApptmts(apptmtType)));
                }
                else {
                    numberLabel.setText(String.valueOf(Appointments.allAppointmentsLength()));
                }
                break;
            case "In-Office":
                //All selected
                if (apptmtMonth > 0 && apptmtType != null) {
                    numberLabel.setText(String.valueOf(inOffice.returnApptmts(apptmtType, apptmtMonth)));
                }
                //Month selected, type is not
                else if (apptmtMonth > 0 && apptmtType == null) {
                    numberLabel.setText(String.valueOf(inOffice.returnApptmts(apptmtMonth)));

                }
                //Type selected, month is not
                else if (apptmtMonth == 0 && apptmtType != null) {
                    numberLabel.setText(String.valueOf(inOffice.returnApptmts(apptmtType)));
                }
                else {
                    numberLabel.setText(String.valueOf(InOffice.allAppointmentsLength()));

                }

                break;
            case "Remote":
                //All selected
                if (apptmtMonth > 0 && apptmtType != null) {
                    numberLabel.setText(String.valueOf(remote.returnApptmts(apptmtType, apptmtMonth)));
                }
                //Month selected, type is not
                else if (apptmtMonth > 0 && apptmtType == null) {
                    numberLabel.setText(String.valueOf(remote.returnApptmts(apptmtMonth)));

                }
                //Type selected, month is not
                else if (apptmtMonth == 0 && apptmtType != null) {
                    numberLabel.setText(String.valueOf(remote.returnApptmts(apptmtType)));
                }
                else {
                    numberLabel.setText(String.valueOf(Remote.allAppointmentsLength()));
                }
                break;
        }
    }


    public void onActionVenueSelection(ActionEvent actionEvent) {
        venueSelection = venueCombo.getSelectionModel().getSelectedItem();
    }


    /**
     * This is the initialize method. This method will populate the appointment types, venue,
     * and months ComboBox.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthCombo.setItems(allMonths);
        typeCombo.setItems(types);
        venueCombo.setItems(venueList);
    }


}
