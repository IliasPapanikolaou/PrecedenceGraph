package com.unipi.precedence_graph;

import java.util.List;

public class Data {
    private String simulation;
    private String threadName;
    private long timeElapsed;
    private String dependencies;

    public Data(Process process, long nodeNum, long timeElapsed) {
        this.simulation = "Node " + nodeNum;
        this.threadName = process.getName();
        this.timeElapsed = timeElapsed;
        this.dependencies = getDependenciesName(process.getDependencies());
    }

    public String getBlockData(Data data) {
        String result = simulation + " Process : " + data.threadName + " Dependecies :" + data.dependencies + " time elapsed : " + timeElapsed;
        System.out.println(result);
        return result;
    }

    private String getDependenciesName(List<Process> processes) {
        if (processes.size() != 0) {
            String listOfDependencies = "[";
            for (Process p : processes) {
                listOfDependencies = listOfDependencies + p.getName() + ", ";
            }
            listOfDependencies = listOfDependencies.replaceAll(", $", "") + "]";
            System.out.println(listOfDependencies);
            return listOfDependencies;
        } else {
            return "none";
        }
    }
}
