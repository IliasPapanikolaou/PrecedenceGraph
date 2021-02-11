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
    private String processesData;
    private long timeStamp;
    private int nonce;
    private List<Process> completedProcess;

    public Block(String previousHash, String processesData, long timeStamp) {
        this.previousHash = previousHash;
        this.processesData = processesData;
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
                String.valueOf(nonce) + processesData;
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

}