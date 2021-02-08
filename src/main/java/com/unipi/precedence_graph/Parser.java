package com.unipi.precedence_graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Parser implements ReadPrecedenceFiles {

    //Default Constructor
    public Parser(){
    }

    public List<Process> readPrecedenceFiles(){
        String processName;
        int procWaitTime;
        List<Process> processes = new ArrayList<>();

        try {
            Scanner p_precedence = new Scanner(new File("src/main/resources/p_precedence.txt"));
            Scanner p_timings = new Scanner(new File("src/main/resources/p_timings.txt"));

            System.out.println("================ p_precedence.txt ===============");
            //p_precedence.txt parser
            while(p_precedence.hasNext("(?i)(P\\d+)")){
                processName = p_precedence.next();
                System.out.print("I am " +processName);
                if(p_precedence.hasNext("(?i)\\s*waitfor\\s*")){

                    //add secondary processName to secondaryProcesses list
                    Process process = new Process.Builder(processName).isGenesis(false).build();
                    System.out.print(" and I am waiting for ");
                    p_precedence.next();

                    if(p_precedence.hasNext("(?i)(P\\d+)\\s*(,\\s*P\\d+)+")){
                        List<String> precedenceList = Arrays.asList(p_precedence.next().split("\\s*,"));

                        for(String p : precedenceList){
                            //add dependency processes
                            process.addDependencies(p);
                            if (precedenceList.indexOf(p)==0) System.out.print(p);
                            else if (precedenceList.indexOf(p) == precedenceList.size()-1) System.out.println(" and "+p);
                            else System.out.print(" and " +p);
                        }
                        //add process to precesses list
                        processes.add(process);
                    }
                    else {
                        processName = p_precedence.next();
                        process.addDependencies(processName);
                        System.out.println(processName);
                        processes.add(process);
                    }
                }
                else {
                    //add genesis processName to genesisProcesses list
                    processes.add(new Process.Builder(processName).isGenesis(true).build());
                    System.out.println(" and I am not waiting anyone!");
                }
            }

            System.out.println("================ p_timings.txt ==================");
            //p_timings.txt parser
            while(p_timings.hasNext("(?i)(P\\d+)")){
                processName = p_timings.next();
                System.out.print("I am " +processName);
                if(p_timings.hasNext("\\d+")){
                    procWaitTime = p_timings.nextInt();
                    for (Process p : processes) {
                        if (p.getName().equals(processName)) p.setWaitTime(procWaitTime);
                    }
                    System.out.println(" and I have to sleep for " +procWaitTime +" milliseconds");
                }
                else {
                    System.out.println();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return processes;
    }
}
