/*
 * UCF COP3330 Fall 2021 Assignment 4 Solution
 * Copyright 2021 Tyler Snowdon
 */

package ucf.assignments;

import com.google.gson.Gson;
import ucf.assignments.utils.FileUtils;
import ucf.assignments.utils.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskList {

    private String name;
    private ArrayList<Task> tasks;

    public TaskList(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public List<Long> getTaskTimes() {
        return tasks.stream().map(Task::getDueDate).map(Date::getTime).collect(Collectors.toList());
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
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
        return tasks.contains(task);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task getTask(String formatted) {
        for (Task value : tasks) {
            if (value.getFormatted().equalsIgnoreCase(formatted)) {
                return value;
            }
        }
        return null;
    }

    public void updateTask(Task input) {
        tasks.add(input);
        Logger.debug("Task updated %s", tasks);
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    public void serialize() {
        Gson output = new Gson();
        String toJson = output.toJson(this);
        FileUtils.write(name + ".json", toJson);
        Logger.debug("%s has been serialized to %s", name, FileUtils.LIST_PATH + name + ".json");
    }

    public static TaskList deserialize(String path) {
        Gson input = new Gson();
        return input.fromJson(FileUtils.read(path), TaskList.class);
    }

    public void clearTasks() {
        tasks = new ArrayList<>();
    }
}
