package controller;

import DAO.AppointmentsQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Appointments;
import model.InOffice;
import model.Remote;
import model.Times;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class displays appointments in a TableView. The user can add, update, or delete appointments.
 * There are paths from this class to CustomersInfo and Reports.
 */
public class AppointmentsInfoController implements Initializable {
    @FXML
    private TableView<Appointments> appointmentsTable;
    @FXML
    private TableColumn<Appointments, Integer> appIdCol;
    @FXML
    private TableColumn<Appointments, String> contactCol;
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
    @FXML
    private ComboBox<String> dateCombo;
    @FXML
    private ComboBox<String> venueCombo;
    @FXML
    private TextField apptmtSearch;
    @FXML
    private Button refreshBtn;
    @FXML
    private Label noResults;
    private static ObservableList<String> dateTypesList = FXCollections.observableArrayList("All", "By Week", "By Month");
    private static ObservableList<String> venueList = FXCollections.observableArrayList("All", "Remote", "In-Office");
    String venueSelection = ""; //can remove
    String dateSelection = "";//can remove
    Stage stage;
    Parent scene;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //Temporary values
    Remote remote = new Remote();
    InOffice inOffice = new InOffice();
    Appointments appointments = new Appointments();


    /**
     * This method changes the scene to the AddAppointment scene.
     *
     * @param event The add button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionAddApptmt(ActionEvent event) throws IOException {
        SceneSwitcher AddAppointmentScene = new SceneSwitcher();
        AddAppointmentScene.buttonSwitchScene("/view/AddAppointment.fxml", event);
    }

    /**
     * This method deletes an appointment.
     *
     * @param event The delete button is clicked.
     * @throws SQLException
     */
    @FXML
    public void onActionDeleteApptmt(ActionEvent event) throws SQLException, IOException {
        Appointments apptmtForDelete = appointmentsTable.getSelectionModel().getSelectedItem();

        if (apptmtForDelete == null) {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("No Selection Made");
            noSelection.setContentText("No selection made. Please select an appointment to be deleted.");
            noSelection.showAndWait();
        } else {
            Alert apptmtDeleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            apptmtDeleteConfirmation.setTitle("Confirm Appointment Deletion");
            apptmtDeleteConfirmation.setHeight(400);
            apptmtDeleteConfirmation.setContentText("Please confirm you would like to delete the " + apptmtForDelete.getType() + " appointment with the ID : " + apptmtForDelete.getApptmtId() + ".");
            Optional<ButtonType> result = apptmtDeleteConfirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                int thisApptmtId = apptmtForDelete.getApptmtId();
                int rowsAffected = AppointmentsQuery.delete(thisApptmtId);

                if (rowsAffected > 0) {
                    Alert deleteSuccess = new Alert(Alert.AlertType.INFORMATION);
                    deleteSuccess.setTitle("Appointment Deleted");
                    deleteSuccess.setHeight(400);
                    deleteSuccess.setContentText("Appointment deletion has succeeded for the " + apptmtForDelete.getType() + " appointment with the ID : " + apptmtForDelete.getApptmtId() + ".");
                    deleteSuccess.showAndWait();

                    Appointments.deleteAppointment(apptmtForDelete);

                    if (LocalDateTime.now().isBefore(apptmtForDelete.getStart())) {
                        String newCancellation = "Appointment with ID: " + apptmtForDelete.getApptmtId() + " for the contact \""
                                + apptmtForDelete.getContactName() + "\" has been cancelled on "
                                + dateFormat.format(Times.localToUTC(LocalDateTime.now())) + " UTC.";

                        String fileName = "src/appointment_cancellations.txt";
                        FileWriter fileWriter = new FileWriter(fileName, true);
                        PrintWriter outputFile = new PrintWriter(fileWriter);
                        outputFile.println(newCancellation);
                        outputFile.close();
                    }
                }
            } else {
                Alert deleteFailed = new Alert(Alert.AlertType.INFORMATION);
                deleteFailed.setTitle("Delete Failed");
                deleteFailed.setHeight(400);
                deleteFailed.setContentText("Appointment deletion has failed for the " + apptmtForDelete.getType() + " appointment with the ID : " + apptmtForDelete.getApptmtId() + ".");
                deleteFailed.showAndWait();
            }

        }

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
     * This method takes in a selection from the appointmentsTable TableView and passes the selected appointment
     * to the UpdateAppointment scene. The scene is changed to the UpdateAppointment scene.
     *
     * @param event The Update button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionUpdateApptmt(ActionEvent event) throws IOException {
        if (appointmentsTable.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/UpdateAppointment.fxml"));
            loader.load();

            UpdateAppointmentController UAController = loader.getController();
            UAController.sendAppointment(appointmentsTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("No Selection Made");
            noSelection.setContentText("No selection made. Please select an appointment to be updated.");
            noSelection.showAndWait();
        }
    }

    /**
     * This method displays all appointments in the appointmentsTable TableView.
     *
     * @param event The All Appointments RadioButton is selected.
     */
    @FXML
    public void onActionAll(ActionEvent event) {
        appointmentsTable.setItems(Appointments.getAllAppointments());
    }

    /**
     * This method displays appointments within one month of the current date in the appointmentsTable TableView.
     *
     * @param event The By Month RadioButton is selected.
     */
    @FXML
    public void onActionMonth(ActionEvent event) {
        LocalDate dateNow = LocalDate.now();
        //appointmentsTable.setItems(Appointments.selectMonth(dateNow));
        appointmentsTable.setItems(appointments.selectMonth(dateNow));
    }

    /**
     * This method terminates the application.
     *
     * @param event The exit button is clicked.
     */
    public void onActionExit(ActionEvent event) {
        System.exit(0);
    }

    /**
     * This method displays appointments within one week of the current date in the appointmentsTable TableView.
     *
     * @param event The By Week RadioButton is selected.
     */
    @FXML
    public void onActionWeek(ActionEvent event) {
        LocalDate dateNow = LocalDate.now();
        //appointmentsTable.setItems(Appointments.selectWeek(dateNow));
        appointmentsTable.setItems(appointments.selectWeek(dateNow));
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


    public void onActionRefresh(ActionEvent actionEvent) {
        LocalDate dateNow = LocalDate.now();


        if (venueCombo.getSelectionModel().getSelectedItem() != null) {
            venueSelection = venueCombo.getSelectionModel().getSelectedItem();
        }

        if (dateCombo.getSelectionModel().getSelectedItem() != null) {
            dateSelection = dateCombo.getSelectionModel().getSelectedItem();
        }

        switch (dateSelection) {
            case "":
                appointmentsTable.setItems(appointments.getAllAppointments());
            case "All":
                if (venueSelection.equals("Remote")) {
                    appointmentsTable.setItems(remote.getAllAppointments());
                } else if (venueSelection.equals("In-Office")) {
                    appointmentsTable.setItems(inOffice.getAllAppointments());
                } else {
                    appointmentsTable.setItems(Appointments.getAllAppointments());
                }
                break;
            case "By Week":
                if (venueSelection.equals("Remote")) {
                    appointmentsTable.setItems(remote.selectWeek(dateNow));
                } else if (venueSelection.equals("In-Office")) {
                    appointmentsTable.setItems(inOffice.selectWeek(dateNow));
                } else {
                    appointmentsTable.setItems(appointments.selectWeek(dateNow));
                }
                break;
            case "By Month":
                if (venueSelection.equals("Remote")) {
                    appointmentsTable.setItems(remote.selectMonth(dateNow));
                } else if (venueSelection.equals("In-Office")) {
                    appointmentsTable.setItems(inOffice.selectMonth(dateNow));
                } else {
                    appointmentsTable.setItems(appointments.selectMonth(dateNow));
                }
                break;
        }
    }

    public void onActionDateSelection(ActionEvent actionEvent) {
        LocalDate dateNow = LocalDate.now();

        dateSelection = dateCombo.getSelectionModel().getSelectedItem();

        switch (dateSelection) {
            case "All":
                if (venueSelection.equals("Remote")) {
                    appointmentsTable.setItems(remote.getAllAppointments());
                } else if (venueSelection.equals("In-Office")) {
                    appointmentsTable.setItems(inOffice.getAllAppointments());
                } else {
                    appointmentsTable.setItems(Appointments.getAllAppointments());
                }
                break;
            case "By Week":
                if (venueSelection.equals("Remote")) {
                    appointmentsTable.setItems(remote.selectWeek(dateNow));
                } else if (venueSelection.equals("In-Office")) {
                    appointmentsTable.setItems(inOffice.selectWeek(dateNow));
                } else {
                    appointmentsTable.setItems(appointments.selectWeek(dateNow));
                }
                break;
            case "By Month":
                if (venueSelection.equals("Remote")) {
                    appointmentsTable.setItems(remote.selectMonth(dateNow));
                } else if (venueSelection.equals("In-Office")) {
                    appointmentsTable.setItems(inOffice.selectMonth(dateNow));
                } else {
                    appointmentsTable.setItems(appointments.selectMonth(dateNow));
                }
                break;
        }

    }

    public void onActionVenueSelection(ActionEvent actionEvent) {
        venueSelection = venueCombo.getValue();

        LocalDate dateNow = LocalDate.now();

        switch (venueSelection) {
            case "In-Office":
                if (dateSelection.equals("By Month")) {
                    appointmentsTable.setItems(inOffice.selectMonth(dateNow));
                    break;
                } else if (dateSelection.equals("By Week")) {
                    appointmentsTable.setItems(inOffice.selectWeek(dateNow));
                    break;
                } else {
                    appointmentsTable.setItems(inOffice.getAllAppointments());
                    break;
                }

            case "Remote":
                if (dateSelection.equals("By Month")) {
                    appointmentsTable.setItems(remote.selectMonth(dateNow));
                    break;
                } else if (dateSelection.equals("By Week")) {
                    appointmentsTable.setItems(remote.selectWeek(dateNow));
                    break;
                } else {
                    appointmentsTable.setItems(remote.getAllAppointments());
                    break;
                }

            case "All":
                if (dateSelection.equals("By Month")) {
                    appointmentsTable.setItems(appointments.selectMonth(dateNow));
                    break;
                } else if (dateSelection.equals("By Week")) {
                    appointmentsTable.setItems(appointments.selectWeek(dateNow));
                    break;
                } else {
                    appointmentsTable.setItems(appointments.getAllAppointments());
                    break;
                }

        }


    }

    public void onActionSearchApptmt(ActionEvent actionEvent) {
        noResults.setText("");

        try {
            int thisApptmtSearch = Integer.parseInt(apptmtSearch.getText());
            Appointments returnedApptmt = Appointments.lookupAppointment(thisApptmtSearch);

            if (returnedApptmt != null) {
                appointmentsTable.getSelectionModel().select(returnedApptmt);
            }
            else {
                appointmentsTable.getSelectionModel().clearSelection();
                noResults.setText("No results found");
            }
        } catch (NumberFormatException ex) {
            String thisApptmtSearch = apptmtSearch.getText();

            appointmentsTable.setItems(Appointments.lookupAppointment(thisApptmtSearch));

            if(!Appointments.resultsFound()) {
                noResults.setText("No results found");
            }

        }
    }


    /**
     * This is the initialize method.
     * This method will populate the appointments TableView with all appointments.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentsTable.setItems(Appointments.getAllAppointments());
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

        dateCombo.setItems(dateTypesList);
        venueCombo.setItems(venueList);

        Image refresh = new Image("view/Refresh-button-icon.jpg");
        ImageView iv1 = new ImageView(refresh);
        iv1.setFitWidth(20);
        iv1.setFitHeight(20);
        refreshBtn.setGraphic(iv1);
    }


}





