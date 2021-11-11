/*
 * UCF COP3330 Fall 2021 Assignment 4 Solution
 * Copyright 2021 Tyler Snowdon
 */

package ucf.assignments;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import ucf.assignments.utils.DateUtils;
import ucf.assignments.utils.Logger;

import java.net.URL;
import java.util.*;

public class FXMLController implements Initializable {

    public Button importListButton;
    public Button exportListButton;
    public Button clearListButton;
    public Button newTaskButton;

    public Button deleteTaskButton;
    public Button updateTaskButton;

    public TextField listNameTextField;
    public TextField newTaskDescriptionTextField;
    public TextField newTaskDateField;

    public ComboBox<String> selectViewComboBox;

    public ListView<Task> taskListView;
    @FXML
    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setCurrentTaskList(new TaskList("TaskList"));
        listNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                App.getCurrentTaskList().setName(newValue);
                Logger.debug("[listNameTextField] \"%s\" List is now \"%s\"", oldValue, newValue);
        });
        render();
    }

    public void render() {
        Logger.debug("rendering.");
        clearTextFields();
        displayStatusView();
        displayCurrentTaskList();
    }

    public void displayStatusView() {
        if (selectViewComboBox.getItems().isEmpty()) {
            selectViewComboBox.getItems().addAll(StatusType.getDisplayNames());
        }
    }

    public void clearTextFields() {
        newTaskDateField.clear();
        newTaskDescriptionTextField.clear();
    }

    public void displayCurrentTaskList() {
        TaskList curr = App.getCurrentTaskList();
        if (curr != null) {
            Logger.debug("rendering.. %s", curr.getTasks());
            listNameTextField.setText(curr.getName());
            displayTasks(curr);
        }
    }

    public void displayTasks(TaskList list) {
        Logger.debug("rendering...");
        taskListView.getItems().clear();
        Map<Task, ObservableValue<Boolean>> map = new HashMap<>();
        StatusType currView = App.getCurrentView();
        Logger.debug("-> Now viewing %s tasks", currView != null ? currView.getDisplayName() : "DEFAULT");
        for (Task task : list.getTasks()) {
            if (currView == null || task.getStatus().matches(currView)) {
                map.put(task, new SimpleBooleanProperty(false));
            }
        }

        taskListView.setEditable(false);
        taskListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        taskListView.getItems().addAll(map.keySet());

        taskListView.setCellFactory(CheckBoxListCell.forListView(new Callback<Task, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Task task) {
                BooleanProperty observable = new SimpleBooleanProperty(task.getStatus().getDisplayValue());
                observable.addListener((obs, before, after) -> {
                    task.setStatus(after ? StatusType.COMPLETE : StatusType.NOT_COMPLETE);
                });
                return observable;
            }
        }));
    }

    public void importList(ActionEvent actionEvent) {
        String fileName = listNameTextField.getText();
        try {
            App.setCurrentTaskList(TaskList.deserialize(fileName + ".json"));
            render();
        } catch (Exception e) {
            if (Logger.isDebug()) {
                e.printStackTrace();
            }
            Logger.debug("Error importing \"%s\"...", fileName);
        }
    }

    public void exportList(ActionEvent actionEvent) {
        if (!App.getCurrentTaskList().isEmpty()) {
            App.getCurrentTaskList().serialize();
        }
    }

    public void newList(ActionEvent actionEvent) {
        String newListName = listNameTextField.getText();

        if (newListName.isEmpty() || newListName.isBlank()) {
            Logger.debug("No input detected.");
            return;
        }

        if (App.getCurrentTaskList().getName().equals(newListName)) {
            Logger.debug("The List %s already exists", newListName);
            return;
        }

        TaskList newList = new TaskList(newListName);
        App.setCurrentTaskList(newList);
        Logger.debug("List %s created", newListName);
        render();
    }

    public void selectView(ActionEvent actionEvent) {
        String viewValue = selectViewComboBox.getValue();
        StatusType viewType = StatusType.getType(viewValue);
        if (viewType != null) {
            App.setCurrentView(viewType);
        }
        render();
    }

    public void newTask(ActionEvent actionEvent) {
        String desc = newTaskDescriptionTextField.getText();
        String date = newTaskDateField.getText();

        if (desc.isEmpty()) {
            return;
        }

        if (desc.length() > Task.MAX_DESC_LENGTH) {
            Logger.debug("MAX DESC LENGTH HAS BEEN REACHED");
            return;
        }

        if (App.getCurrentTaskList().containsTask(desc)) {
            return;
        }

        Date taskDate = DateUtils.parse(date);
        Task newTask = new Task(desc, taskDate);
        App.getCurrentTaskList().addTask(newTask);
        App.setCurrentTask(newTask);
        render();
    }

    public void deleteTask(ActionEvent actionEvent) {
        if (App.getCurrentTask() != null) {
            Logger.debug("Task \"%s\" had been deleted", App.getCurrentTask().getDescription());
            App.getCurrentTaskList().removeTask(App.getCurrentTask());
            App.clearCurrentTask();
            render();
        }
    }

    public void updateTask(ActionEvent actionEvent) {
        String desc = newTaskDescriptionTextField.getText();
        String date = newTaskDateField.getText();

        if (desc.isEmpty() && date.isEmpty()) {
            return;
        }

        Task curr = App.getCurrentTask();
        if (!desc.isEmpty()) {
            curr.setDescription(desc);
        }

        if (!date.isEmpty()) {
            curr.setDueDate(DateUtils.parse(date));
        }

        //  App.updateCurrentTaskList(curr);

        render();
    }

    public void listViewClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() != null) {
            if (mouseEvent.getTarget() instanceof CheckBoxListCell) {
                String taskText = ((CheckBoxListCell) mouseEvent.getTarget()).getText();
                if (App.getCurrentTaskList().containsTask(taskText)) {
                    App.setCurrentTask(App.getCurrentTaskList().getTask(taskText));
                }
            }
        }
    }

    public void clearList(ActionEvent actionEvent) {
        App.getCurrentTaskList().clearTasks();
        render();
    }
}
