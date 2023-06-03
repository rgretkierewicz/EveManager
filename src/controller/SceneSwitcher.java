package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is used to change scenes within the application.
 */
public class SceneSwitcher {

    /**
     * This method switches the scene in the application.
     *
     * @param fxml  The name of the fxml document for the scene to be switched to.
     * @param event The action that caused the scene switch.
     * @throws IOException
     */
    public void buttonSwitchScene(String fxml, ActionEvent event) throws IOException {
        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fxml));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
