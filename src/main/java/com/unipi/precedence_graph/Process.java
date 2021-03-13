package com.unipi.precedence_graph;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *     Process class extends thread class.
 *     Each process object that is created has a name and set as genesis or not.
 *     Genesis process is the first process of the graph (starting point).
 *
 *     Dependencies list contains the processes that must be executed before the current process.
 *
 *     When each process is executed, if dependencies list is not empty,
 *     the calling thread goes into waiting state. It remains in waiting
 *     state until the referenced threads finish their task.
 *     This achieved by making the process to wait its dependent processes found
 *     in the list dependencies to ".join()"
 *
 *     As soon as all the previous tasks have "joined", starting time is set,
 *     and if the process has waitTime then
 *     the process is paused for predefined time using "sleep()" method.
 *     (Sleep is used to emulate a task that, otherwise, the thread had to do)
 *
 *     When the sleeping time (task) is over, finishing time is set and
 *     process object is added to blockOrder list with its execution timeStamp.
 *     This achieved by using the "addProcessToBlockOrder(Process process)"
 *     synchronized method so the static "blockOrder" List can be accessed once at a time when
 *     threads race to access it.
 *
 *     Builder design pattern was uses for the Process class.
 *
 */

public class Process extends Thread {

    private final String name;
    private final boolean isGenesis;
    private int waitTime;
    private List<Process> dependencies;
    private long executionTimeStamp;

    public Process(Builder builder){
        this.name = builder.name;
        this.isGenesis = builder.isGenesis;
        this.dependencies = new ArrayList<>();
    }

    //Getters
    public boolean isGenesisProcess(){
        return this.isGenesis;
    }

    public int getWaitTime(){
        return this.waitTime;
    }

    public List<Process> getDependencies() { return this.dependencies; }

    public String getProcessName() { return this.name; }

    public long getExecutionTimeStamp() { return  this.executionTimeStamp; }

    //Setter
    public void setWaitTime(int waitTime){
        this.waitTime = waitTime;
    }

    public void addDependencies(Process process) {this.dependencies.add(process); }

    @Override
    public void run() {
        Instant start = null;
        Instant finish;
        try {
            if(!this.dependencies.isEmpty()){
                for (Process p : dependencies){
                    //System.out.println("[" +this.name +"] I have to wait [" +p.name +"]");
                    p.join();
                }
            }
            //System.out.println("[" +this.name +"] I am sleeping for "+this.waitTime +"ms");
            start = Instant.now();
            this.executionTimeStamp = start.getEpochSecond();
            sleep(waitTime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("[" +Thread.currentThread().getName() +"] finished after " +this.waitTime +"ms");
        finish = Instant.now();
        long waitedIdle = Duration.between(PrecedenceGraph.globalTimerStart, start).toMillis();
        long timeElapsed = Duration.between(PrecedenceGraph.globalTimerStart,finish).toMillis();
        System.out.println("[" +this.name +"] finished. Time elapsed: " +(double)timeElapsed/1000
                            +" seconds, waited idle for: " +(double)waitedIdle/1000 +" seconds");

        //Add Process to static list blockOrder
        addProcessToBlockOrder(this);
    }

    //Builder
    public static class Builder{
        private final String name;
        private boolean isGenesis;

        public Builder(String name){
            this.name = name;
        }

        public Builder isGenesis(Boolean isGenesis){
            this.isGenesis = isGenesis;
            return this;
        }

        public Process build(){
            return new Process(this);
        }

    }

    //Synchronized method so the blockOrder List can be accessed once at a time
    private static synchronized void addProcessToBlockOrder(Process process){
        PrecedenceGraph.blockOrder.add(process);
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                '}';
    }
}