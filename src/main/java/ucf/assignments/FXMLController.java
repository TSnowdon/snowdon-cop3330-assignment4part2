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
import ucf.assignments.utils.DateUtils;
import ucf.assignments.utils.Logger;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
    public String selectSorted = "Sort By Due Dates";

    public ListView<Task> taskListView;
    @FXML
    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setCurrentTaskList(new TaskList("TaskList"));
        listNameTextField.textProperty().addListener((observable, previousValue, newValue) -> {
            App.getCurrentTaskList().setName(newValue);
            Logger.debug("[listNameTextField] \"%s\" List is now \"%s\"", previousValue, newValue);
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
            selectViewComboBox.getItems().add(selectSorted);
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
        ArrayList<Task> map = new ArrayList<>();
        ArrayList<Task> tasks = list.getTasks();

        StatusType currView = App.getCurrentView();
        String currText = "DEFAULT";
        if (selectViewComboBox.getValue() != null) {
            if (selectViewComboBox.getValue().equals(selectSorted)) {
                currText = "SORTING";
                currView = null;
                Logger.debug("[sorting] before sorting... %s", tasks);
                tasks = tasks.stream().sorted(Task::compareTo)
                        .collect(Collectors.toCollection(ArrayList::new));

                Collections.reverse(tasks);
                Logger.debug("[sorting] after sorting... %s", tasks);
            }
        }
        Logger.debug("-> Now viewing %s tasks", currView != null ? currView.getDisplayName() : currText);
        for (Task task : tasks) {
            if (currView == null || task.getStatus().matches(currView)) {
                map.add(task);
            }
        }

        taskListView.setEditable(false);
        taskListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        taskListView.getItems().addAll(map);

        taskListView.setCellFactory(CheckBoxListCell.forListView(task -> {
            BooleanProperty observable = new SimpleBooleanProperty(task.getStatus().getDisplayValue());
            observable.addListener((observableValue, previousValue, newValue) -> {
                task.setStatus(newValue ? StatusType.COMPLETE : StatusType.NOT_COMPLETE);
                Logger.debug("Task \"%s\" went from \"%s\" to \"%s\"", task.getDescription(), StatusType.getType(previousValue), StatusType.getType(newValue));
            });
            return observable;
        }));
    }

    public void importList() {
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

    public void exportList() {
        if (!App.getCurrentTaskList().isEmpty()) {
            App.getCurrentTaskList().serialize();
        }
    }

    public void selectView() {
        String viewValue = selectViewComboBox.getValue();
        StatusType viewType = StatusType.getType(viewValue);
        if (viewType != null) {
            Logger.debug("[selectView] setting viewType...");
            App.setCurrentView(viewType);
        }
        render();
    }

    public void newTask() {
        String desc = newTaskDescriptionTextField.getText();
        String date = newTaskDateField.getText();

        if (desc.isEmpty() || desc.isBlank()) {
            newTaskDescriptionTextField.clear();
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
        Logger.debug("[newTask] %s %s", newTask.getDescription(), newTask.getDueDate());
        App.getCurrentTaskList().addTask(newTask);
        App.setCurrentTask(newTask);
        render();
    }

    public void deleteTask() {
        if (App.getCurrentTask() != null) {
            Logger.debug("Task \"%s\" had been deleted", App.getCurrentTask().getDescription());
            App.getCurrentTaskList().removeTask(App.getCurrentTask());
            App.clearCurrentTask();
            render();
        }
    }

    public void updateTask() {
        String desc = newTaskDescriptionTextField.getText();
        String date = newTaskDateField.getText();

        if (desc.isEmpty() && date.isEmpty()) {
            return;
        }

        Task curr = App.getCurrentTask();
        if (!desc.isEmpty()) {
            if (!desc.isBlank()) {
                curr.setDescription(desc);
            }
        }

        if (!date.isEmpty()) {
            if (!date.isBlank()) {
                curr.setDueDate(DateUtils.parse(date));
            }
        }

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

    public void clearList() {
        App.getCurrentTaskList().clearTasks();
        render();
    }
}
