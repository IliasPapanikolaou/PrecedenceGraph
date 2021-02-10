package com.unipi.precedence_graph;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PrecedenceGraph {
    public static int prefix = 2;
    public static List<Block> blockChain = new ArrayList<>();

    public static void main(String[] args) {

        List<Process> processes = new ArrayList<>(new Parser().readPrecedenceFiles());

        System.out.println("================== Summarize ====================");
        int countGenesis = 0;
        int countSecondary = 0;
        for (Process p : processes) {
            if (p.isGenesisProcess()) countGenesis++;
            else countSecondary++;
        }

        System.out.println("Number of genesis processes: " + countGenesis);
        System.out.println("Number of secondary processes: " + countSecondary);

        for (Process p : processes) {
            if (p.getWaitTime() != 0) System.out.println(p.getName() + " has to wait for " + p.getWaitTime() + "ms");
        }

        System.out.println("============== Starting Threads =================");
        if (countGenesis > 0) {
           Block genesisBlock = new Block("0",processes,new Date().getTime());
           genesisBlock.executeProcces();
           genesisBlock.mineBlock(prefix);
            blockChain.add(genesisBlock);
            System.out.println("Node:"+(blockChain.size()-1)+" created");

        }else{
            System.out.println("You must define at least one independent starting thread.");
        }


    }
}
