package com.unipi.precedence_graph;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public String mineBlock(int prefix){
        String prefixString = new String(new char[prefix]).replace('\0','0');
        while (!hash.substring(0,prefix).equals(prefixString)){
            nonce++;
            hash = calculateBlockHash();
        }
        System.out.println("Block is mined.");
        return hash;
    }
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
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b: bytes)
            buffer.append(String.format("%02x",b));

        return buffer.toString();
    }

    public void executeProcces(){
        // φιλτράρω ποιά threads είναι ανεξάρτητα και δεν εχουν ολοκληρωθεί για να τρέξουν πρώτα
        Predicate<Process> independentProcessFilter = Process::isGenesisProcess;
        Predicate<Process> completedProcessFilter = Process::isCompleted;
        Process independentProcess = processes.stream().filter(completedProcessFilter.negate().and(independentProcessFilter)).findFirst().orElse(null);
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



    }
}
