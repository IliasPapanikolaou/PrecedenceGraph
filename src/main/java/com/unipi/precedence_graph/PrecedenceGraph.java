package com.unipi.precedence_graph;

import java.util.ArrayList;
import java.util.List;

public class PrecedenceGraph {

    public static void main(String[] args) {

        List<Process> processes = new ArrayList<>(new Parser().readPrecedenceFiles());


        System.out.println("================== Summarize ====================");
        int countGenesis = 0;
        int countSecondary = 0;
        for (Process p : processes){
            if (p.isGenesisProcess()) countGenesis++;
            else countSecondary++;
        }

        System.out.println("Number of genesis processes: " +countGenesis);
        System.out.println("Number of secondary processes: " +countSecondary);

        for (Process p: processes){
            if (p.getWaitTime() != 0) System.out.println(p.getName() +" has to wait for " +p.getWaitTime() +"ms");
        }

    }
}
