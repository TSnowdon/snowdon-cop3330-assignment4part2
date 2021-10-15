/*
 * UCF COP3330 Fall 2021 Assignment 4 Solution
 * Copyright 2021 Tyler Snowdon
 */

package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLController implements Initializable {

    public Button importListButton;
    public Button exportListButton;
    @FXML
    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void importListButtonAction(ActionEvent actionEvent) {
        /**
         *  Open File Explorer Prompt for selecting the File
         *      If the File is a valid JSON
         *          Use GSON to deserialize it into a TaskList
         *      If not
         *          Do nothing
         */
    }

    public void exportListButtonAction(ActionEvent actionEvent) {
        /**
         *  Take the currently selected TaskList in the App
         *      If a JSON doesn't exists with the name of the TaskList
         *          run the TaskList through a GSON serializer
         *      If not
         *          tell the user to rename the file
         */
    }

    public void addTaskButtonAction(ActionEvent actionEvent){
        /**
         *  If the values in the Text Field can construct a Task
         *      create a Task using those values
         *      insert that Task into the current TaskList in the App
         *  If not
         *      tell the user that they are missing some values
         */
    }
}