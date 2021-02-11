package com.unipi.precedence_graph;

import java.util.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.unipi.precedence_graph.PrecedenceGraph.blockChain;

public class Process extends Thread {
    private final String name;
    private final boolean isGenesis;
    private int waitTime;
    private List<Process> depedencies;
    private Data threadData;

    public Process(Builder builder) {
        this.name = builder.name;
        this.isGenesis = builder.isGenesis;
        this.depedencies = new ArrayList<>();
    }

    //Getters
    public boolean isGenesisProcess() {
        return this.isGenesis;
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public List<Process> getDependencies() {
        return this.depedencies;
    }

    public Data getData() {
        return this.threadData;
    }

    public String getProcessName() {
        return this.name;
    }

    //Setter
    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void addDependencies(Process process) {
        this.depedencies.add(process);
    }

    public void setData(Data threadData) {
        this.threadData = threadData;
    }

    @Override
    public void run() {
        Instant start = null;
        Instant finish;
        try {
            if (!this.depedencies.isEmpty()) {
                for (Process p : depedencies) {
                    //System.out.println("[" +this.name +"] I have to wait [" +p.name +"]");
                    p.join();
                }
            }
            //System.out.println("[" +this.name +"] I am sleeping for "+this.waitTime +"ms");
            start = Instant.now();
            sleep(waitTime);

            //return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("[" +Thread.currentThread().getName() +"] finished after " +this.waitTime +"ms");
        finish = Instant.now();
        long waitedIdle = Duration.between(PrecedenceGraph.globalTimerStart, start).toMillis();
        long timeElapsed = Duration.between(PrecedenceGraph.globalTimerStart, finish).toMillis();
        System.out.println("[" + this.name + "] finished. Time elapsed: " + (double) timeElapsed / 1000
                + " seconds, waited idle for: " + (double) waitedIdle / 1000 + " seconds");
        Data currentThreadData = new Data(this, this.getId(), timeElapsed);
        this.setData(currentThreadData);
    }

    //Builder
    public static class Builder {
        private final String name;
        private boolean isGenesis;

        public Builder(String name) {
            this.name = name;
        }

        public Builder isGenesis(Boolean isGenesis) {
            this.isGenesis = isGenesis;
            return this;
        }

        public Process build() {
            return new Process(this);
        }

    }

}