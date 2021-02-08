package com.unipi.precedence_graph;

import java.util.ArrayList;
import java.util.List;

public class Process implements Runnable {

    private final String name;
    private final boolean isGenesis;
    private int waitTime;
    private List<String> depedencies;

    public Process(Builder builder){
        this.name = builder.name;
        this.isGenesis = builder.isGenesis;
        this.depedencies = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.waitTime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[" +Thread.currentThread().getName() +"] finished after " +this.waitTime +"ms");
    }

    //Getters
    public boolean isGenesisProcess(){
        return this.isGenesis;
    }

    public String getName(){
        return this.name;
    }

    public int getWaitTime(){
        return this.waitTime;
    }

    public List<String> getDependencies() {return depedencies;}

    //Setter
    public void setWaitTime(int waitTime){
        this.waitTime = waitTime;
    }

    public void addDependencies(String process) {this.depedencies.add(process);}

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