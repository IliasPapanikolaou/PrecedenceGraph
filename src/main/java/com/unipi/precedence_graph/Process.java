package com.unipi.precedence_graph;

import java.util.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.unipi.precedence_graph.PrecedenceGraph.blockChain;

public class Process extends Thread {
    public static final int prefix = 2;
    private final String name;
    private final boolean isGenesis;
    private int waitTime;
    private List<Process> depedencies;

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
        //to block θα χτιστεί αφού ολοκληρωθεί το νήμα εφόσον μας χρειάζεται η χρονική του διάρκεια
        //πρέπει να εξασφαλίσουμε ότι το genesis thread είναι μονο 1 για να είναι και το block μονο 1
        if (this.isGenesis) {
            Data data = new Data(this, 0, timeElapsed);
            String processData = data.getBlockData(data);
            Block genesisBlock = new Block("0", processData, new Date().getTime());
            genesisBlock.mineBlock(prefix);
            blockChain.add(genesisBlock);
            //todo προσθήκη στη βάση δεδομένων
        } else {
            Data data = new Data(this, blockChain.size() - 1, timeElapsed);
            String processData = data.getBlockData(data);
            Block block = new Block(blockChain.get(blockChain.size() - 1).getHash(), processData, new Date().getTime());
            block.mineBlock(prefix);
            blockChain.add(block);
            //todo προσθήκη block στη βάση δεδομένων

        }
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