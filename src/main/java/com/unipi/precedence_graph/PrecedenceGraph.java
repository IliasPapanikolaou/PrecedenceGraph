package com.unipi.precedence_graph;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class PrecedenceGraph {

    public static Instant globalTimerStart;

    //Ioanna
    public static int prefix = 2;
    public static List<Block> blockChain = new ArrayList<>();

    public static void main(String[] args) {

        List<Process> processes = new ArrayList<>(new Parser().readPrecedenceFiles());

        System.out.println("================== Precedence ====================");
        int countGenesis = 0;
        int countSecondary = 0;
        for (Process p : processes){
            if (p.isGenesisProcess()) countGenesis++;
            else countSecondary++;
        }

        System.out.println("Number of genesis processes: " +countGenesis);
        System.out.println("Number of secondary processes: " +countSecondary);

        for (Process p: processes){
            if (p.getWaitTime() != 0) System.out.println(p.getProcessName() +" should sleep for " +p.getWaitTime() +"ms");
            if (!p.getDependencies().isEmpty()){
                System.out.print(p.getProcessName() +" has to wait for ");
                for (Process proc : p.getDependencies()){
                    System.out.print(proc.getProcessName() +" ");
                }
                System.out.println();
            }
        }

        System.out.println("=============== Starting Threads =================");
        globalTimerStart = Instant.now();
        for (Process p: processes){
            p.start();
        }

        //Ioanna
//        if (countGenesis > 0) {
//            Block genesisBlock = new Block("0",processes,new Date().getTime());
//            genesisBlock.executeProcces();
//            genesisBlock.mineBlock(prefix);
//            blockChain.add(genesisBlock);
//            System.out.println("Node:"+(blockChain.size()-1)+" created");
//
//        }else{
//            System.out.println("You must define at least one independent starting thread.");
//        }

    }
}
