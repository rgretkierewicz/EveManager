package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.Appointments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * This class displays a report of appointments that were cancelled before their appointment time.
 */
public class AppointmentCancellationController implements Initializable {
    @FXML
    ListView<String> listView = new ListView<>();
    Appointments appointments = new Appointments();

    /**
     * This method changes the scene to the AppointmentsInfo scene.
     *
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
     *
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
     *
     * @param event The Customers Info button is clicked.
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
     * This is the initialize method.
     * This method will populate the ListView with all canceled appointments.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String fileName = "src/appointment_cancellations.txt", item;
        File file = new File(fileName);
        Scanner inputFile = null;

        try {
            inputFile = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        while (inputFile.hasNext()) {
                item = inputFile.nextLine();
                appointments.addCancellation(item);
        }


        listView.setItems(appointments.getAppointmentCancellations());
    }


}
