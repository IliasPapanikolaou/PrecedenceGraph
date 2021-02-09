package com.unipi.precedence_graph;

import java.util.ArrayList;
import java.util.List;

public class Process extends Thread {

    private final String name;
    private final boolean isGenesis;
    private int waitTime;
    private List<Process> depedencies;

    public Process(Builder builder){
        this.name = builder.name;
        this.isGenesis = builder.isGenesis;
        this.depedencies = new ArrayList<>();
    }

    //Getters
    public boolean isGenesisProcess(){
        return this.isGenesis;
    }

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

    @Override
    public void run() {
        try {
            if(!this.depedencies.isEmpty()){
                for (Process p : depedencies){
                    System.out.println("[" +this.name +"] I have to wait [" +p.name +"]");
                    p.join();
                }
            }
            System.out.println("[" +this.name +"] I am sleeping for "+this.waitTime +"ms");
            sleep(waitTime);
            return;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[" +Thread.currentThread().getName() +"] finished after " +this.waitTime +"ms");
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