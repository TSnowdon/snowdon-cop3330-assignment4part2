/*
 * UCF COP3330 Fall 2021 Assignment 4 Solution
 * Copyright 2021 Tyler Snowdon
 */

package ucf.assignments;

import ucf.assignments.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class TaskList {

    private String name;
    private HashMap<UUID, Task> tasks;

    public TaskList(String name) {
        this.name = name;
        this.tasks = new HashMap<>();
    }

    public HashMap<UUID, Task> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<UUID, Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void removeTask(Task task) {
        tasks.remove(task.getId());
        System.out.println(tasks);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean containsTask(String desc) {
        return getTask(desc) != null;
    }

    public boolean containsTask(Task task) {
        return tasks.containsKey(task.getId());
    }

    public Task getTask(String formatted) {
        for (Task value : tasks.values()) {
            if (value.getFormatted().equalsIgnoreCase(formatted)) {
                return value;
            }
        }
        return null;
    }

    public Task getTask(UUID uuid) {
        return tasks.get(uuid);
    }

    public void updateTasks(Task curr) {
        /**
         * For each Task in tasks
         * check if there dueDate has passed
         * if there dueDate has passed update there status
         */
    }

    public void updateTask(Task input) {
        tasks.put(input.getId(), input);
        Logger.debug("Task updated %s", tasks.values());
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    public void clearTasks() {
        tasks = new HashMap<>();
    }
}
