/*
 * UCF COP3330 Fall 2021 Assignment 4 Solution
 * Copyright 2021 Tyler Snowdon
 */

import org.junit.jupiter.api.Test;
import ucf.assignments.App;
import ucf.assignments.StatusType;
import ucf.assignments.Task;
import ucf.assignments.TaskList;
import ucf.assignments.utils.DateUtils;
import ucf.assignments.utils.FileUtils;
import ucf.assignments.utils.Logger;

import java.io.File;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NEEDED TEST CASES
 * A user shall be able to add a new 2do list ✓
 * A user shall be able to remove an existing 2do list ✓
 * A user shall be able to edit the title of an existing 2do list ✓
 * A user shall be able to add a new item to an existing 2do list ✓
 * A user shall be able to remove an item from an existing 2do list ✓
 * A user shall be able to edit the description of an item within an existing 2do list  ✓
 * A user shall be able to edit the due date of an item within an existing 2do list  ✓
 * A user shall be able to mark an item in a 2do list as complete ✓
 * A user shall be able to display all of the existing items in a 2do list  ✓
 * A user shall be able to display only the incompleted items in a 2do list ✓
 * A user shall be able to display only the completed items in a 2do list  ✓
 * A user shall be able to save all of the items in a single 2do list to external storage ✓
 * A user shall be able to save all of the items across all of the 2do lists to external storage ✓
 * A user shall be able to load a single 2do list that was previously saved to external storage  ✓
 * A user shall be able to load multiple 2do lists that were previous saved to external storage  ✓
 */

public class AppTest {
    /**
     * ✓ Create a TaskList "tasklist"
     * ✓ Create a Task "task1"
     * ✓ Create a Task "task2"
     * ✓ Add "task1" to "tasklist"
     * <p>
     * ✓ Test addition of setting the current tasklist in App
     * ✓ est the ability to update the name of the tasklist from the "tasks" in App
     * ✓ Test addition of task to the current TaskList in App
     * ✓ Test removal of a task from the current TaskList in App
     * ✓ Test setting the description of a task from the current TaskList in App
     * ✓ Test setting the date of a task from the currently TaskList in App
     * ✓ Test setting the status of a task from the currently TaskList in App
     * ✓ Test listing all items in a TaskList
     * ✓ Test listing the completed items in a TaskList
     * ✓ Test listing the incomplete items in a TaskList
     * ✓ Test importing a TaskList
     * ✓ Test exporting a TaskList
     */
    @Test
    public void test() {

        TaskList test = new TaskList("test");
        Task task1 = new Task("task1", new Date());
        Task task2 = new Task("task2", new Date());

        // Test Task insertion and creation
        assertDoesNotThrow(() -> {
            test.addTask(task1);
            App.setCurrentTaskList(test);
            App.setCurrentTask(task1);
            App.getCurrentTaskList().setName("testtasklist");
            App.getCurrentTaskList().addTask(task2);
            App.getCurrentTaskList().removeTask(task2);
            App.getCurrentTaskList().getTask(task1.getFormatted()).setDescription("new task");
            App.getCurrentTaskList().getTask(task1.getFormatted()).setDueDate(DateUtils.parse("2010-11-11"));
            App.getCurrentTaskList().getTask(task1.getFormatted()).setStatus(StatusType.COMPLETE);
            // Testing NOT printing
            for (Task task : App.getCurrentTaskList().getTasks()) {
                Logger.debug(task.getFormatted());
                assertNotNull(task);
            }
            // Testing Complete printing
            for (Task task : App.getCurrentTaskList().getTasks()) {
                if (task.getStatus().matches(StatusType.COMPLETE)) {
                    Logger.debug(task.getFormatted());
                    assertNotNull(task);
                }
            }
            // Testing NOT Complete printing
            for (Task task : App.getCurrentTaskList().getTasks()) {
                if (task.getStatus().matches(StatusType.NOT_COMPLETE)) {
                    Logger.debug(task.getFormatted());
                    assertNotNull(task);
                }
            }
        });
        // Testing export
        test.serialize();
        assertTrue(new File(FileUtils.LIST_PATH + "testtasklist.json").exists());
        // Testing import
        assertNotNull(TaskList.deserialize("testtasklist.json"));
    }
}
