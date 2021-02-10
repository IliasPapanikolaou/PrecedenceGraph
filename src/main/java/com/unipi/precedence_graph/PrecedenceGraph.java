package com.unipi.precedence_graph;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PrecedenceGraph {

    public static Instant globalTimerStart;

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

    }
}
