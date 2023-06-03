package controller;

import DAO.CustomersQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Countries;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class displays customer information in a TableView. The user can add, update, or delete customers.
 */
public class CustomersInfoController implements Initializable {
    @FXML
    private TableView<Customers> customersTable;
    @FXML
    private TableColumn<Customers, String> addressCol;
    @FXML
    private TableColumn<Countries, String> countryCol;
    @FXML
    private TableColumn<Customers, Integer> custIdCol;
    @FXML
    private TableColumn<Customers, Integer> fldCol;
    @FXML
    private TableColumn<Customers, String> nameCol;
    @FXML
    private TableColumn<Customers, String> pcCol;
    @FXML
    private TableColumn<Customers, String> phoneCol;
    @FXML
    private TextField customerSearch;
    @FXML
    private Label noResults;

    /**
     * This method changes the scene to the AppointmentsInfo scene.
     *
     * @param event The Appointments Info button is clicked
     * @throws IOException
     */
    public void onActionAppointmentsInfo(ActionEvent event) throws IOException {
        SceneSwitcher AppointmentsInfoScene = new SceneSwitcher();
        AppointmentsInfoScene.buttonSwitchScene("/view/AppointmentsInfo.fxml", event);
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
     * This method takes in a selection from the appointmentsTable TableView and passes the selected appointment
     * to the UpdateAppointment scene. The scene is changed to the UpdateAppointment scene.
     *
     * @param event The Update button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionUpdateCustomer(ActionEvent event) throws IOException {
        if (customersTable.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/UpdateCustomer.fxml"));
            loader.load();

            UpdateCustomerController UCController = loader.getController();
            UCController.sendCustomer(customersTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("No Selection Made");
            noSelection.setContentText("No selection made. Please select a customer to be updated.");
            noSelection.showAndWait();
        }

    }

    /**
     * This method changes the scene to the AddCustomer scene.
     *
     * @param event The Add button is clicked.
     * @throws IOException
     */
    public void onActionAddCustomer(ActionEvent event) throws IOException {
        SceneSwitcher AddCustomerScene = new SceneSwitcher();
        AddCustomerScene.buttonSwitchScene("/view/AddCustomer.fxml", event);
    }

    /**
     * This method deletes a customer.
     *
     * @param event The Delete button is clicked.
     * @throws IOException
     * @throws SQLException
     */
    public void onActionDeleteCustomer(ActionEvent event) throws IOException, SQLException {
        Customers customerForDelete = customersTable.getSelectionModel().getSelectedItem();

        boolean customerAppointments = false;

        if (customerForDelete == null) {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("No Selection Made");
            noSelection.setContentText("No selection made. Please select a customer to be deleted.");
            noSelection.showAndWait();
        }

        for (Appointments apptmt : Appointments.getAllAppointments()) {
            if (apptmt.getCustId() == customerForDelete.getCustId()) {
                customerAppointments = true;
            }
        }
        if (customerAppointments == true) {
            Alert customerDeleteError = new Alert(Alert.AlertType.ERROR);
            customerDeleteError.setTitle("Customer Deletion Error");
            customerDeleteError.setContentText("This customer has scheduled appointments. Please return to the appointments page to cancel the appointments associated with this customer and try again.");
            customerDeleteError.showAndWait();
        }

        if (!customerAppointments) {
            Alert confirmDeleteCustomer = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDeleteCustomer.setTitle("Confirm Customer Deletion");
            confirmDeleteCustomer.setHeight(400);
            confirmDeleteCustomer.setContentText("Please confirm you would like to delete the customer: \"" + customerForDelete.getCustName() + "\".");
            Optional<ButtonType> result = confirmDeleteCustomer.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                int thisCustId = customerForDelete.getCustId();
                int rowsAffected = CustomersQuery.delete(thisCustId);

                if (rowsAffected > 0) {
                    Alert deleteSuccess = new Alert(Alert.AlertType.INFORMATION);
                    deleteSuccess.setTitle("Customer Deleted");
                    deleteSuccess.setContentText("The customer: \"" + customerForDelete.getCustName() + "\"" + " has been successfully deleted.");
                    deleteSuccess.showAndWait();

                    Customers.deleteCustomer(customerForDelete);
                } else {
                    Alert deleteFailed = new Alert(Alert.AlertType.CONFIRMATION);
                    deleteFailed.setTitle("Delete Failed");
                    deleteFailed.setContentText("Customer deletion has failed for the customer: \"" + customerForDelete.getCustName() + "\".");
                    deleteFailed.showAndWait();
                }

            }
        }
    }


    /**
     * This method takes user input and searches for customers that match that input. User input can be a customer id or a customer name.
     * If there is a customer with a matching id, that customer is selected from the table.
     * If there is a customer or customers with a matching name, only those customers are shown in the table.
     * If no results are found, "No results found" is displayed above the search field.
     *
     * @param actionEvent the search field receives input
     */
    public void onActionSearchCustomer(ActionEvent actionEvent) {
        noResults.setText("");

        try {
            int thisCustSearch = Integer.parseInt(customerSearch.getText());
            Customers returnedCust = Customers.lookupCustomer(thisCustSearch);

            if (returnedCust != null) {
                customersTable.getSelectionModel().select(returnedCust);
            } else {
                customersTable.getSelectionModel().clearSelection();
                noResults.setText("No results found");
            }

        } catch (NumberFormatException ex) {
            String thisCustSearch = customerSearch.getText();

            customersTable.setItems(Customers.lookupCustomer(thisCustSearch));

            if (!Customers.resultsFound()) {
                noResults.setText("No results found");
            }

        }
    }

    /**
     * This is the initialize method.
     * This method will populate the customers TableView with all customers.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customersTable.setItems(Customers.getAllCustomers());
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("custName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        pcCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        fldCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
    }

}
