package com.unipi.precedence_graph;

import org.javatuples.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 *
 *     read txt files :
 *     ****************
 *     p_precedence.txt --> import process dependencies
 *     ....................................................
 *     for each process contained in p_precedence.txt a new Process object is created
 *     - if process has no dependencies it's name is saved and it is set to be genesis
 *     - if process has dependencies it's name is saved, it is set not to be genesis and
 *       each of the processes that has to wait for is saved in its precedenceList
 *     p_timings.txt --> import process timings
 *     ............................................
 *     while reading timings file, each process that has timing defined in file,
 *     sets it waitTime to that value. Processes are checked based on their name
 *
 *     Method readFiles() urges user to select between different graphs found in resources folder,
 *     new graph files placed in resource folder are automatically detected.
 *
 *     Method readPrecedenceFiles() Parses the p_precedence.txt and p_timings.txt
 *     using regular expressions.
 *
 **/

public class Parser implements ReadPrecedenceFiles {

    //Default Constructor
    public Parser(){
    }

    public List<Process> readPrecedenceFiles(){
        String processName;
        int procWaitTime;
        List<Process> processes = new ArrayList<>();

        try {
            Pair<String, String> graphFiles = readFiles();
            Scanner p_precedence = new Scanner(new File("src/main/resources/"+graphFiles.getValue0()));
            Scanner p_timings = new Scanner(new File("src/main/resources/"+graphFiles.getValue1()));

            System.out.println("================ p_precedence.txt ===============");
            //p_precedence.txt parser
            while(p_precedence.hasNext("(?i)(P\\d+)")){
                processName = p_precedence.next();
                System.out.print("I am " +processName);
                if(p_precedence.hasNext("(?i)\\s*waitfor\\s*")){

                    //add processName to processes list
                    Process process = new Process.Builder(processName).isGenesis(false).build();
                    System.out.print(" and I am waiting for ");
                    p_precedence.next();

                    if(p_precedence.hasNext("(?i)(P\\d+)\\s*(,\\s*P\\d+)+")){
                        List<String> precedenceList = Arrays.asList(p_precedence.next().split("\\s*,"));

                        for(String p : precedenceList){
                            //add dependency processes
                            for(Process proc : processes){
                                if (proc.getProcessName().equalsIgnoreCase(p)){
                                    process.addDependencies(proc);
                                }
                            }
                            if (precedenceList.indexOf(p)==0) System.out.print(p);
                            else if (precedenceList.indexOf(p) == precedenceList.size()-1) System.out.println(" and "+p);
                            else System.out.print(" and " +p);
                        }
                        //add process to precesses list
                        processes.add(process);
                    }
                    else {
                        processName = p_precedence.next();
                        //add dependency process
                        for(Process proc : processes){
                            if (proc.getProcessName().equalsIgnoreCase(processName)){
                                process.addDependencies(proc);
                            }
                        }
                        processes.add(process);
                        System.out.println(processName);
                    }
                }
                else {
                    //add genesis process to processes list
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
                        if (p.getProcessName().equals(processName)){
                            p.setWaitTime(procWaitTime);
                        }
                    }
                    System.out.println(" and I have to work for " +procWaitTime +" milliseconds");
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

    private Pair<String, String> readFiles() throws FileNotFoundException{

        File folder = new File("src/main/resources");
        File[] files = folder.listFiles(((dir,name) -> name.toLowerCase().startsWith("p_precedence")));
        //Default Graph
        String fileP = "p_precedence.txt";
        String fileT = "p_timings.txt";
        System.out.println("Please choose a Precedence Graph (or press 0 to exit):");
        int numOfFiles = 0;
        for(int i = 0; i < (files != null ? files.length : 0); i++){
            numOfFiles = i+1;
            System.out.println(numOfFiles +". " +files[i].getName());
        }
        //User input
        Scanner input = new Scanner(System.in);
        if(input.hasNextInt()){
            int num = input.nextInt();
            if(num == 0) System.exit(0);
            else if(num > 0 && num <= numOfFiles){
                fileP = files[num-1].getName();
                String s = "p_timings" +fileP.substring(12);
                files = folder.listFiles(f -> f.getName().equals(s));
                if (files != null) {
                    fileT = Arrays.stream(files).findFirst().orElseThrow(FileNotFoundException::new).getName();
                }
                else {
                    System.out.println("The corresponding " +s +" file not found.");
                    System.exit(0);
                }
            }
            else{
                System.out.println("Wrong expression, I will use the default graphs");
            }
        }
        else{
            System.out.println("Wrong expression, I will use the default graphs");
        }
        return new Pair<>(fileP, fileT);
    }
}
