package com.unipi.precedence_graph;

public class Process {

    private final String name;
    private final boolean isGenesis;
    private int waitTime;

    public Process(Builder builder){
        this.name = builder.name;
        this.isGenesis = builder.isGenesis;
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

    //Setter
    public void setWaitTime(int waitTime){
        this.waitTime = waitTime;
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