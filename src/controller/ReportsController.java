package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * This class is used to allow the user to navigate to the three available reports, Appointment Reports,
 * Cancellation Reports, and Contact Schedule Reports.
 */
public class ReportsController {

    /**
     * This method changes the scene to the AppointmentReports scene.
     * @param event The Appointment Reports button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionAppointmentReports(ActionEvent event) throws IOException {
        SceneSwitcher AppointmentReportsScene = new SceneSwitcher();
        AppointmentReportsScene.buttonSwitchScene("/view/AppointmentReports.fxml", event);
    }

    /**
     * This method changes the scene to the AppointmentInfo scene.
     * @param event The Appointments Info button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionAppointmentsInfo(ActionEvent event) throws IOException {
        SceneSwitcher AppointmentsInfoScene = new SceneSwitcher();
        AppointmentsInfoScene.buttonSwitchScene("/view/AppointmentsInfo.fxml", event);
    }

    /**
     * This method returns the application to the Reports scene.
     * @param event The Back button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionCancellationReports(ActionEvent event) throws IOException {
        SceneSwitcher AppointmentCancellationsScene = new SceneSwitcher();
        AppointmentCancellationsScene.buttonSwitchScene("/view/AppointmentCancellations.fxml", event);
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

    /**
     * This method changes the scene to the ScheduleReports scene.
     * @param event The Contact Schedule Reports button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionScheduleReports(ActionEvent event) throws IOException {
        SceneSwitcher ScheduleReportsScene = new SceneSwitcher();
        ScheduleReportsScene.buttonSwitchScene("/view/ScheduleReports.fxml", event);
    }

    /**
     * This method terminates the application.
     * @param event The exit button is clicked.
     */
    public void onActionExit(ActionEvent event) {
        System.exit(0);
    }

}
