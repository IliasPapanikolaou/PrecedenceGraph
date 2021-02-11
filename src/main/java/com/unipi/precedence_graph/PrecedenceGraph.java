package com.unipi.precedence_graph;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PrecedenceGraph {

    public static Instant globalTimerStart;
    public static  List<Process> blockOrder = new ArrayList<>();
    public static  List<Block> blockChain = new ArrayList<>();

    public static void main(String[] args){

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
            if (p.getWaitTime() != 0) System.out.println(p.getProcessName() +" has to work for " +p.getWaitTime() +"ms");
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

        //Wait for the last process to finish before continue
        try {
            processes.get(processes.size()-1).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("============= Creating BlockChain ================");
        new Thread(()->{
            for (Process p: blockOrder){
                BlockChain.createBlock(p, "emulation1");
            }
            //print blockchain
            for (Block b : blockChain) System.out.println(b.toString());
        }).start();


    }
}
