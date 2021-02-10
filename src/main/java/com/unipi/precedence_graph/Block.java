package com.unipi.precedence_graph;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> origin/master
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Block {
    private String hash;
    private String previousHash;
    private List<Process> processes;
    private long timeStamp;
    private int nonce;
    private List<Process> completedProcess;

    public Block(String previousHash, List<Process> processes, long timeStamp) {
        this.previousHash = previousHash;
        this.processes = processes;
        this.timeStamp = timeStamp;
        hash = calculateBlockHash();
    }
<<<<<<< HEAD

    public String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hash.substring(0, prefix).equals(prefixString)) {
=======
    public String mineBlock(int prefix){
        String prefixString = new String(new char[prefix]).replace('\0','0');
        while (!hash.substring(0,prefix).equals(prefixString)){
>>>>>>> origin/master
            nonce++;
            hash = calculateBlockHash();
        }
        System.out.println("Block is mined.");
        return hash;
    }
<<<<<<< HEAD

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String calculateBlockHash() {
        String dataToHash = previousHash + String.valueOf(timeStamp) +
                String.valueOf(nonce) + processes;
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
=======
    public String getHash() {
        return hash;
    }
    public String getPreviousHash() {
        return previousHash;
    }
    public String calculateBlockHash(){
        String dataToHash = previousHash + String.valueOf(timeStamp)+
                String.valueOf(nonce) + processes;
        MessageDigest digest = null;
        byte[] bytes = null;
        try
        {
>>>>>>> origin/master
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
<<<<<<< HEAD
        for (byte b : bytes)
            buffer.append(String.format("%02x", b));
=======
        for (byte b: bytes)
            buffer.append(String.format("%02x",b));
>>>>>>> origin/master

        return buffer.toString();
    }

<<<<<<< HEAD
    public void executeProcces() {
=======
    public void executeProcces(){
>>>>>>> origin/master
        // φιλτράρω ποιά threads είναι ανεξάρτητα και δεν εχουν ολοκληρωθεί για να τρέξουν πρώτα
        Predicate<Process> independentProcessFilter = Process::isGenesisProcess;
        Predicate<Process> completedProcessFilter = Process::isCompleted;
        Process independentProcess = processes.stream().filter(completedProcessFilter.negate().and(independentProcessFilter)).findFirst().orElse(null);
<<<<<<< HEAD
        if (independentProcess != null) {
            System.out.println("independent thread is " + independentProcess.getName());
            independentProcess.run();
            independentProcess.setCompleted(true);
            System.out.println("independent thread is completed " + independentProcess.isCompleted());

        } else {
            System.out.println("dependent threads are runing ");
            // φιλτράρω ποια threads έχουν ολοκληρωθεί
            List<Process> completedProcesses = processes.stream().filter(completedProcessFilter).collect(Collectors.toList());
            System.out.println("Completed processes are "+completedProcesses.size());
            List<String> completedProcessesNames = new ArrayList<>();
            // κρατάω τα ονόματα των thread που έχουν ολοκληρωθεί για να τα συγκρίνω με την λίστα depedencies του κάθε thread
            for (Process p : completedProcesses) {
                completedProcessesNames.add(p.getName());
                System.out.println("process "+p.getName()+" added to completedProcessesNames list");
            }
            // φιλτράρω ποιά threads είναι εξαρτώμενα και δεν εχουν ολοκληρωθεί
            List<Process> dependentProcesses = processes.stream().filter(completedProcessFilter.negate().and(independentProcessFilter).negate()).collect(Collectors.toList());
            System.out.println("dependent processes are "+dependentProcesses.size());
            //λέγχω την dependency list του κάθε thread και αν έχουν ολοκληρωθεί τα προηγούμενα το τρέχω και βγαίνω από το for loop
            //γιατί θέλω να τρέξει μόνο ένα thread σε κάθε block
            for (Process p : dependentProcesses) {
                System.out.println("process "+p.getName()+" checking names");
                List<String> dependencies = p.getDependencies();
                System.out.println("process "+p.getName()+" number of dependencies : "+dependencies.size());
                if (completedProcesses.containsAll(dependencies)) {
                    p.run();
                    p.setCompleted(true);
                    break;
                }
                break;
            }


//            Process processtoExecute = processes.stream().filter(ascendantProcessesCompleted).findFirst().orElse(null);
//            if(processtoExecute!=null){
//                processtoExecute.run();
//                processtoExecute.setCompleted(true);
//            }
        }


=======
        if(independentProcess!=null){
            System.out.println("independent thread is "+independentProcess.getName());
            independentProcess.run();
            independentProcess.setCompleted(true);
            System.out.println("independent thread is completed "+independentProcess.isCompleted());

        }else {
            Predicate<Process> ascendantProcessesCompleted = new Predicate<Process>() {
                @Override
                public boolean test(Process process) {
                    boolean ascendantProcessCompleted = false;
                    List<Process> ascendantsName = process.getDependencies(); // βρίσκω τα ονοματα των εργασιών που
                    // πρέπει να εχουν ολοκληρωθεί
                    for(Process processName:ascendantsName){ // για καθε μία από αυτές βρίσκω με βάση το όνομα το Οbject process
                        Predicate<Process> processNameFilter = processFilter -> process.getProcessName().equals(processName);
                        System.out.println("process name "+processName);
                        Process ascendantProcess = processes.stream().filter(processNameFilter).findFirst().orElse(null);
                        ascendantProcessCompleted = ascendantProcess.isCompleted();
                        System.out.println("ascendant process completed "+ascendantProcessCompleted);
                        if(!ascendantProcessCompleted)
                            break;
                    }
                    return ascendantProcessCompleted;
                }
            };
            Process processtoExecute = processes.stream().filter(ascendantProcessesCompleted).findFirst().orElse(null);
            if(processtoExecute!=null){
                processtoExecute.run();
                processtoExecute.setCompleted(true);
            }
        }



>>>>>>> origin/master
    }
}
