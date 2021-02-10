package com.unipi.precedence_graph;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Process extends Thread {

    private final String name;
    private final boolean isGenesis;
    private boolean isCompleted;
    private int waitTime;
    private List<Process> depedencies;

    public Process(Builder builder){
        this.name = builder.name;
        this.isGenesis = builder.isGenesis;
        this.isCompleted = false;
        this.depedencies = new ArrayList<>();
    }

    //Getters
    public boolean isGenesisProcess(){
        return this.isGenesis;
    }

    public boolean isCompleted(){ return this.isCompleted; }

    public int getWaitTime(){
        return this.waitTime;
    }

    public List<Process> getDependencies() {return this.depedencies;}

    public String getProcessName() { return this.name; }

    //Setter
    public void setWaitTime(int waitTime){
        this.waitTime = waitTime;
    }

    public void addDependencies(Process process) {this.depedencies.add(process);}

    public void setCompleted(boolean isCompleted) {this.isCompleted=isCompleted;}

    @Override
    public void run() {
        Instant start = null;
        Instant finish;
        try {
            if(!this.depedencies.isEmpty()){
                for (Process p : depedencies){
                    //System.out.println("[" +this.name +"] I have to wait [" +p.name +"]");
                    p.join();
                }
            }
            //System.out.println("[" +this.name +"] I am sleeping for "+this.waitTime +"ms");
            start = Instant.now();
            sleep(waitTime);
            //return;
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

}