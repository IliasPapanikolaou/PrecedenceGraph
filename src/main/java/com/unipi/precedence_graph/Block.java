package com.unipi.precedence_graph;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Block {
    private String hash;
    private final String previousHash;
    private final String emulationName;
    private final String processName;
    private final String executionTime;
    private final String dependencies;
    private final long timeStamp;
    private int nonce;
    private List<Process> completedProcess;

    public Block(String previousHash, String emulationName, String processName,
                 String executionTime, String dependencies, long timeStamp) {
        this.previousHash = previousHash;
        this.emulationName = emulationName;
        this.processName = processName;
        this.executionTime = executionTime;
        this.dependencies = dependencies;
        this.timeStamp = timeStamp;
        hash = calculateBlockHash();
    }

    //Constructor
    public String mineBlock(int prefix){
        String prefixString = new String(new char[prefix]).replace('\0','0');
        while (!hash.substring(0,prefix).equals(prefixString)){
            nonce++;
            hash = calculateBlockHash();
        }
        System.out.println("Block is mined.");
        return hash;
    }

    //Getters
    public String getHash() {
        return hash;
    }
    public String getPreviousHash() {
        return previousHash;
    }

    public String calculateBlockHash(){
        String dataToHash = previousHash +emulationName +executionTime +dependencies
                +timeStamp
                +nonce;
        MessageDigest digest;
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

    @Override
    public String toString() {
        return "Block{" +
                "hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", emulationName='" + emulationName + '\'' +
                ", processName='" + processName + '\'' +
                ", executionTime='" + executionTime + '\'' +
                ", dependencies='" + dependencies + '\'' +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                ", completedProcess=" + completedProcess +
                '}';
    }
}
