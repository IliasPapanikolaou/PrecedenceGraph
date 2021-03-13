package com.unipi.precedence_graph;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 *  Each block when is created gets a prefix of 3 zeros.
 *  Each block's hash is set by calculateBlockHash() method
 *  based on the previous block Hash, block's emulation name,
 *  execution time, dependencies, timeStamp and nonce
 *
 */

public class Block {
    private String hash;
    private final String previousHash;
    private final String emulationName;
    private String processName;
    private int executionTime;
    private String dependencies;
    private long timeStamp;
    private long nonce;

    //Constructor
    public Block(String previousHash, String emulationName, String processName,
                 int executionTime, String dependencies, long timeStamp) {
        this.previousHash = previousHash;
        this.emulationName = emulationName;
        this.processName = processName;
        this.executionTime = executionTime;
        this.dependencies = dependencies;
        this.timeStamp = timeStamp;
        hash = calculateBlockHash();
    }

    //Constructor overload
    public Block(String emulationName, String hash){
        this.emulationName = emulationName;
        this.previousHash = hash;
    }
    //Constructor overload
    public Block(String hash, String previousHash, String emulationName, String processName,
                 int executionTime, String dependencies, long timeStamp, long nonce) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.emulationName = emulationName;
        this.processName = processName;
        this.executionTime = executionTime;
        this.dependencies = dependencies;
        this.timeStamp = timeStamp;
        this.nonce = nonce;
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

    //Getters
    public String getHash() {
        return hash;
    }
    public String getPreviousHash() {
        return previousHash;
    }
    public String getEmulationName() {
        return emulationName;
    }
    public String getProcessName() {
        return processName;
    }
    public int getExecutionTime() {
        return executionTime;
    }
    public String getDependencies() {
        return dependencies;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public long getNonce() {
        return nonce;
    }

    //Block calculation and encryption
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
                '}';
    }
}
